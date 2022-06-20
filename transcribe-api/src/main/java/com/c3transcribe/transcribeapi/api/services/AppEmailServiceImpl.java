package com.c3transcribe.transcribeapi.api.services;

import com.c3transcribe.transcribeapi.api.domian.RegUserVerifyLogDetails;
import com.c3transcribe.transcribeapi.api.repository.RegisteredUsersRepo;
import com.c3transcribe.transcribeapi.api.services.exceptions.EmailNotFoundException;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author Rajesh
 * This class will be responsible for creating unique verification token and also send verification email, password change emails
 * and any other communication emails.
 */
@Service @Data
public class AppEmailServiceImpl implements AppMailService {
    
    private static final Logger logger = getLogger(AppEmailServiceImpl.class);
    @Value("${deployHostName:http://devappserver-api.local:8585/}")
    private String HOSTNAME ;
    Environment env;
    RegisteredUsersRepo registeredUsersRepo;
    SecureRandom secureRandom;
    JavaMailSender javaMailSender;
    //EncryptUtils encryptUtils;
    @Value("${app.support.email}")
    @Email
    private String supportEmailTo ;
    
    private String emailTo ;
    
    @Autowired
    public AppEmailServiceImpl(
            Environment environment,
            SecureRandom secureRandom, RegisteredUsersRepo registeredUsersRepo,
            JavaMailSender javaMailSender)
    //        EncryptUtils encryptUtils)
    {
        this.secureRandom = secureRandom;
        this.env = environment;
        this.registeredUsersRepo = registeredUsersRepo;
        this.javaMailSender = javaMailSender;
      //  this.encryptUtils = encryptUtils;
    }
    
    /**
     * This method only purpose is to decode the email and send email, all random token and other info should eb filled before calling this method.
     *
     * @param userReq
     * @return Boolean
     */
    //@Async
    public RegUserVerifyLogDetails sendVerificationEmailForRegistration(RegUserVerifyLogDetails userReq ) throws MessagingException {
        //TODO: get the app server info
        logger.info("Called with e-mail {}", userReq.getEmail());
        Boolean status = Boolean.FALSE;
        String decodedEmail =
                new String(Base64.getUrlDecoder().decode(userReq.getEmail()), StandardCharsets.UTF_8);
        String randomNum = userReq.getConfEmailToken();
        String verifyEmailToken = new StringBuilder().append(decodedEmail).append("/").append(randomNum).append("/").toString();
        userReq.setConfEmailUrl(getEmailVerificationUrl(verifyEmailToken));
        userReq.setVerified(false);
        
        try {
            
            final MimeMessage message = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            String content = "Dear [[name]],<br>Please click the link below to verify your registration:<br />"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" +
                    "<br>Thank your:<br><footer>C3transcribe.com</footer>";
            content = content.replace("[[URL]]", userReq.getConfEmailUrl());
            helper.setTo(userReq.getEmail());
            helper.setFrom(supportEmailTo);
            helper.setSubject("Thanks For registering. Complete your registration by Activating your Email ");
            helper.setText("Dear [[name]],<br>Please click the link below to verify your registration:<br />"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" +
                    "<br>Thank your:<br><footer>C3transcribe.com</footer>" , true);
            helper.setValidateAddresses(true);
            helper.setReplyTo(supportEmailTo);
            helper.setSentDate(Date.from(Instant.now()));
            javaMailSender.send(helper.getMimeMessage());
            userReq.setVerificationDate(Instant.now());
            userReq.setVerificationEmailSent(true);
            userReq = registeredUsersRepo.save(userReq);
            status = Boolean.TRUE;
        } catch (MessagingException e) {
            status = false;
            userReq.setVerificationEmailSent(false);
            userReq.setVerified(false);
            userReq.setConfEmailUrl("null");
            e.printStackTrace();
            logger.error("Exception occurred while sending conf email {} :: verifyURL: {} ", userReq.getConfEmailUrl());
            //TODO throw an exception saying unable to send email
            throw new MessagingException("Exception occurred while sending user registration conf email, please check your email " + userReq.getConfEmailUrl());
        }
        return userReq;
    }
    
    
    /**
     *
     * This method will send verification email with given token,
     * this is mainly written for login verification email.
     * This method can be enhanced to send any other email with tokens and to use templates.
     *
     * @param userReq
     * @param token
     * @return
     * @throws MessagingException
     */
    public RegUserVerifyLogDetails sendVerificationEmailWithToken(RegUserVerifyLogDetails userReq, String token) throws MessagingException {
        //TODO: get the app server info
        logger.info("Called with e-mail {}", userReq.getEmail());
        Boolean status = Boolean.FALSE;
        String confEmailUrl = getEmailVerificationUrl(token);
        userReq.setConfEmailUrl(confEmailUrl);
        userReq.setVerified(false);
        
        try {
            
            final MimeMessage message = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setTo(userReq.getEmail());
            helper.setSubject("Thanks For registering. Complete your registration by Activating your Email ");
            helper.setText("<p>To activate your account, click the link below:<br/>"
                    + "<a href='" + confEmailUrl + "'>" +
                    "Click here to complete your registration" +
                    "</a></p>", true);
            helper.setValidateAddresses(true);
            helper.setReplyTo(supportEmailTo);
            helper.setFrom("No-Reply@c3transcribe.com");
            helper.setSentDate(Date.from(Instant.now()));
            javaMailSender.send(helper.getMimeMessage());
            userReq.setVerificationDate(Instant.now());
            userReq.setVerificationEmailSent(true);
            userReq = registeredUsersRepo.save(userReq);
            status = Boolean.TRUE;
        } catch (MessagingException e) {
            status = false;
            userReq = null;
            e.printStackTrace();
            logger.error("Exception occurred while sending conf email {}", confEmailUrl);
            //TODO throw an exception saying unable to send email
            throw new MessagingException("Exception occurred while sending conf email, please check your email " + confEmailUrl);
        }
        return userReq;
    }
    
    
    /**
     * Send an e-mail message.
     *
     * @param mailMessage Mail message
     */
    private void sendMail(
            @NotNull final MimeMessage mailMessage
    ) {
        javaMailSender.send(mailMessage);
    }
    
    /**
     *
     * @param email
     * @return
     * @throws MessagingException
     */
    @Override
    public boolean sendEmailForUserRegistration(String email) throws MessagingException {
            Boolean isEmailSent = false;
            logger.debug("User is registering or trying to log for first time %s", email);
            //TODO generate unique code and save in DB or get unique code from DB
            //
            String token = RandomStringUtils.random(60, 0, 0, true, true, null, secureRandom);
            RegUserVerifyLogDetails regUserVerifyLogDetails = new RegUserVerifyLogDetails();
            regUserVerifyLogDetails.setEmail(email);
            regUserVerifyLogDetails.setDisabled(true);
            regUserVerifyLogDetails.setVerified(false);
            //This is the token that will be validated when user send confirmation api request to api server
            // TODO add validity for the token to be completed
            regUserVerifyLogDetails.setConfEmailToken(token);
            regUserVerifyLogDetails = sendVerificationEmailForRegistration(regUserVerifyLogDetails);
            regUserVerifyLogDetails = registeredUsersRepo.save(regUserVerifyLogDetails);
            return isEmailSent;
        
    }
    /**
     * This method will send email after the email is changed in DB with reset token
     * @param orgEmail E-mail address of the recipient
     * @param newEmail new E-mail address of the recipient
     * @param token E-mail change token
     * @return
     */
    @Override
    public RegUserVerifyLogDetails sendMailWhenEmailChangeReqestedWithToken(final @NotBlank @Email String orgEmail, String newEmail, String token) throws MessagingException, EmailNotFoundException {
        List<RegUserVerifyLogDetails> regUserVerifyLogDetailsList = registeredUsersRepo.findByEmail(orgEmail);
        if(CollectionUtils.isEmpty(regUserVerifyLogDetailsList)){
            throw new EmailNotFoundException("Email is not found or not registered!");
        }  else if (CollectionUtils.isNotEmpty(regUserVerifyLogDetailsList) && Objects.nonNull(regUserVerifyLogDetailsList.stream().findFirst()) && Objects.nonNull(regUserVerifyLogDetailsList.stream().findFirst().get())) {
            RegUserVerifyLogDetails regUserVerifyLogDetails = sendVerificationEmailForEmailChange(regUserVerifyLogDetailsList.stream().findFirst().get(), newEmail, token);
            return regUserVerifyLogDetails;
        } else {
            logger.error("Unknown Usecase encountered, for email change token: %s ", orgEmail);
            return null;
        }
    }
    
    /**
     * This method will generate email verification url that can be send to client email
     * @param token
     * @return
     */
    private String getEmailVerificationUrl(final String token) {
        //TODO this will be a token from the config properties
        String verificationUrlBase = "/api/v1/verify-email/{token}";
            verificationUrlBase = verificationUrlBase.replace("{token}", token);
        return  verificationUrlBase;
    }
    
    
    
    @Override
    public RegUserVerifyLogDetails sendMailWithEmailChangeToken(final RegUserVerifyLogDetails regUser, final String email) throws EmailNotFoundException, MessagingException {
        
        return sendMailWithEmailChangeWithOutToken(regUser.getEmail(),email);
    }
    
    @Override
    public RegUserVerifyLogDetails sendMailWithEmailChangeToken(final RegUserVerifyLogDetails regUser, @NotBlank @Email final String email, String token) throws MessagingException {
        return sendVerificationEmailForEmailChange(regUser,email,token);
    }
    @Override
    public RegUserVerifyLogDetails sendMailWithEmailChangeWithOutToken(final @NotBlank @Email String orgEmail, String newEmail) throws MessagingException, EmailNotFoundException {
        logger.debug("New Token generated for email change request for original Email:: %s to cahnge to new Email :: %s ", orgEmail , newEmail);
        String token = RandomStringUtils.random(60, 0, 0, true, true, null, secureRandom);
        //TODO Save the token for that email in DB
        // As MVP for .com launch this will be valid for ever.
        // This can be addressed pre-launch or after launch.
        RegUserVerifyLogDetails regUserVerifyLogDetails;
        List<RegUserVerifyLogDetails> regUserVerifyLogDetailsList = registeredUsersRepo.findByEmail(orgEmail);
        if(regUserVerifyLogDetailsList.isEmpty()){
            throw new UsernameNotFoundException("");
        }else {
            regUserVerifyLogDetails = regUserVerifyLogDetailsList.stream().findFirst().get();
        }
        return sendMailWithEmailChangeToken(regUserVerifyLogDetails, newEmail, token);
    }
    
    
    
    /**
     * @param changeOfEmailUserReq
     * @param email
     * @param token
     * @return
     */
    @Async
    public RegUserVerifyLogDetails sendVerificationEmailForEmailChange(
            RegUserVerifyLogDetails changeOfEmailUserReq,
            @NotBlank @Email final String email,
            @NotBlank final String token
    ) throws MessagingException {
        Boolean result = Boolean.FALSE;
        logger.info("Called with e-mail {}, token {}", email, token);
        String confEmail = null;
        
        //TODO user some email templating engine
        String changeEmailText = "<p>Change e-mail address, click the link below:<br />" +
                " <a href=\"${API_HOST}/api/v1/public/profile/settings/changeEmail?token=${TOKEN}&email= ${EMAIL}\" " +
                "Click here to complete the change of your e-mail.  </a> </p>";
        changeEmailText.replace("${API_HOST}", HOSTNAME);
        changeEmailText.replace("${EMAIL}", email);
        changeEmailText.replace("${TOKEN}", token);
        changeOfEmailUserReq.setVerified(false);
        changeOfEmailUserReq.setDisabled(false);
        changeOfEmailUserReq.setConfEmailToken(token);
        changeOfEmailUserReq.setConfEmailToken(confEmail);
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setTo(email);
        helper.setSubject("Change your e-mail");
        helper.setText(changeEmailText, true);
        
        helper.setValidateAddresses(true);
        helper.setReplyTo(supportEmailTo);
        helper.setSentDate(Date.from(Instant.now()));
        javaMailSender.send(helper.getMimeMessage());
        changeOfEmailUserReq.setEmailSentDate(Instant.now());
        changeOfEmailUserReq = registeredUsersRepo.save(changeOfEmailUserReq);
        result = Boolean.TRUE;
        
        return changeOfEmailUserReq;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithNewPassword(
            @NotBlank @Email final String email,
            @NotBlank final String newPassword
    ) {
        logger.info("Called with e-mail {}, newPassword {}", email, newPassword);
        
        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setTo(email);
            helper.setSubject("Recover password");
            helper.setText("Your new password: " + "<b>" + newPassword + "</b>", true);
            
            helper.setValidateAddresses(true);
            helper.setReplyTo(emailTo);
            helper.setSentDate(Date.from(Instant.now()));
            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithUsername(
            @NotBlank @Email final String email,
            @NotBlank final String username
    ) {
        logger.info("Called with e-mail {}, username {}", email, username);
        
        try {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();
            
            final MimeMessage message = sender.createMimeMessage();
            
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setTo(email);
            helper.setSubject("Recover username");
            helper.setText("Your username: " + "<b>" + username + "</b>", true);
            
            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean sendConfEmailForEmailChange(final String orgEmail, String newEmail) throws MessagingException, EmailNotFoundException {
        return sendMailWithEmailChangeWithOutToken(orgEmail,newEmail).isVerificationEmailSent();
    }
    
    /**
     *
     * @param regEmail
     * @return
     * @throws EmailNotFoundException
     */
    private RegUserVerifyLogDetails getRegisteredUserByEmail( String regEmail) throws EmailNotFoundException {
    
        List<RegUserVerifyLogDetails> regUserVerifyLogDetailsList = registeredUsersRepo.findByEmail(regEmail);
        if(CollectionUtils.isEmpty(regUserVerifyLogDetailsList)){
            throw new EmailNotFoundException("Email is not found or not registered!");
        }  else if (CollectionUtils.isNotEmpty(regUserVerifyLogDetailsList) && Objects.nonNull(regUserVerifyLogDetailsList.stream().findFirst()) && Objects.nonNull(regUserVerifyLogDetailsList.stream().findFirst().get())) {
            return regUserVerifyLogDetailsList.stream().findFirst().get();
        } else {
            logger.error("Unknown Usecase encountered, for email change token: %s ", regEmail);
            return null;
        }
    }
}
