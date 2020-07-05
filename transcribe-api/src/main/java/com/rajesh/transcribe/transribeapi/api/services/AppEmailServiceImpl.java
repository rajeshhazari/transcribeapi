package com.rajesh.transcribe.transribeapi.api.services;

import com.c3transcribe.core.utils.EncryptUtils;
import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import com.rajesh.transcribe.transribeapi.api.repository.RegisteredUsersRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class AppEmailServiceImpl implements  AppMailService{

	private static final Logger logger = getLogger(AppEmailServiceImpl.class);
	
	Environment env;
	RegisteredUsersRepo registeredUsersRepo;
	SecureRandom secureRandom;
	JavaMailSender javaMailSender;
	@Email
	private String emailTo = "transcribeapp@yahoo.com";
	private final String changeEmailText = new StringBuilder("Change e-mail address, click the link below:<br />")
			.append(" <a href='${API_HOST}/api/v1/public/profile/settings/changeEmail?token= ${TOKEN}&email= ${EMAIL}' ")
			.append("Click here to complete the change of your e-mail.  </a> ").toString();

	@Autowired
	public AppEmailServiceImpl(Environment environment, SecureRandom secureRandom, RegisteredUsersRepo registeredUsersRepo, JavaMailSender javaMailSender){
		this.secureRandom = secureRandom;
		this.env = environment;
		this.registeredUsersRepo = registeredUsersRepo;
		this.javaMailSender = javaMailSender;
	}
	
	/**
	 * 
	 * @param userReq
	 * @return Boolean
	 */
	//@Async
	public CompletableFuture<RegisteredUserVerifyLogDetials>  sendVerificationEmail(RegisteredUserVerifyLogDetials userReq) throws MessagingException {
		//TODO: get the app server info
		logger.info("Called with e-mail {}", userReq.getEmail());
		Boolean status = Boolean.FALSE;
		String decodedEmail =
				new String(Base64.getUrlDecoder().decode(userReq.getEmail()), StandardCharsets.UTF_8);
		String randomNum = EncryptUtils.randomToken();
		String confEmailUrl = new StringBuilder().append("https://devappserver:8585/").append(decodedEmail).append("/").append(randomNum).append("/").toString();
		//generate a random number
		userReq.setConfEmailUrl(confEmailUrl);
		userReq.setCode(Integer.parseInt(randomNum));
		userReq.setVerified(false);
		
		try {
			
			final MimeMessage message = javaMailSender.createMimeMessage();
			
			final MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setTo(userReq.getEmail());
			helper.setSubject("Complete your registration");
			helper.setSubject("Activate - Email ");
			helper.setText("To activate your account, click the link below:<br />"
					+ "<a href='" + confEmailUrl+ "'>" +
					"Click here to complete your registration" +
					"</a>", true);
			helper.setValidateAddresses(true);
			helper.setReplyTo(emailTo);
			helper.setSentDate(Date.from(Instant.now()));
			javaMailSender.send(helper.getMimeMessage());
			registeredUsersRepo.save(userReq);
			status = Boolean.TRUE;
		} catch (MessagingException e) {
			status = false;
			userReq = null;
			e.printStackTrace();
			logger.error("Exception occured while sending conf email {}", confEmailUrl);
			//TODO throw an exception saying unable to send email
			throw new MessagingException("Exception occurred while sending conf email, please check your email "+confEmailUrl);
		}
		return CompletableFuture.completedFuture(userReq);
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


	@Override
	public Boolean sendMailWithEmailChangeToken(final @NotBlank @Email String email, @NotBlank String token) {
		if(null == token){
			token = RandomStringUtils.random(60, 0, 0, true, true, null, secureRandom);
		}
		return sendMailWithEmailChangeToken(registeredUsersRepo.findByEmail(email), email, token);
	}

	/**
	 *
	 * @param regUser
	 * @param email
	 * @param token
	 * @return
	 */
	@Async
	@Override
	public Boolean sendMailWithEmailChangeToken(RegisteredUserVerifyLogDetials regUser,
												@NotBlank @Email final String email,
												@NotBlank final String token
	) {
		Boolean result = Boolean.FALSE;
		logger.info("Called with e-mail {}, token {}", email, token);
		String confEmail =  null;
		try {
			changeEmailText.replace("${API_HOST}", "https://devappserver:8585/");
			changeEmailText.replace("${EMAIL}", email);
			changeEmailText.replace("${TOKEN}", token);
			regUser.setVerified(false);
			regUser.setDisabled(false);
			regUser.setConfEmailToken(token);
			regUser.setConfEmailToken(confEmail);
			final MimeMessage message = javaMailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setTo(email);
			helper.setSubject("Change e-mail");
			helper.setText(changeEmailText, true);

			helper.setValidateAddresses(true);
			helper.setReplyTo(emailTo);
			helper.setSentDate(Date.from(Instant.now()));
			javaMailSender.send(helper.getMimeMessage());
			result = Boolean.TRUE;
			
		} catch (MailSendException e) {
			logger.error("Unable to send email to {}", emailTo);
			e.printStackTrace();
			
		}catch (MessagingException ex){
			logger.error("Messaging exception occured while sending email {}", emailTo);
			ex.printStackTrace();
			
		}
		return result;
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
}
