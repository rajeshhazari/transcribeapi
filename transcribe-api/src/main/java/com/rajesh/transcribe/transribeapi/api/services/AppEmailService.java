package com.rajesh.transcribe.transribeapi.api.services;

import com.c3transcribe.core.utils.EncryptUtils;
import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import com.rajesh.transcribe.transribeapi.api.repository.RegisteredUsersRepo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jose4j.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class AppEmailService implements  AppMailService{

	private static final Logger logger = getLogger(AppEmailService.class);
	
	Environment env;
	RegisteredUsersRepo registeredUsersRepo;
	SecureRandom secureRandom;
	JavaMailSender javaMailSender;
	@Email
	private String emailTo = "transcribeapp@yahoo.com";
	
	@Autowired
	public AppEmailService(Environment environment, SecureRandom secureRandom,RegisteredUsersRepo registeredUsersRepo, JavaMailSender javaMailSender){
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
	@Async
	public Boolean sendVerificationEmail(RegisteredUserVerifyLogDetials userReq) throws MessagingException {
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
			helper.setText("To activation your account, click the link below:<br />"
					+ "<a href='" + confEmailUrl+ "'>" +
					"Click here to complete your registration" +
					"</a>", true);
			sendMail(message);
			registeredUsersRepo.save(userReq);
			status = Boolean.TRUE;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("Exception occured while sending conf email {}", confEmailUrl);
			//TODO throw an exception saying unable to send email
			throw new MessagingException("Exception occured while sending conf email, please check your email "+confEmailUrl);
		}
		return status;
	}
	
	
	private Boolean sendEmail(String emailTo, String subject){
		//TODO implement the code to send email
		return true;
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
	 * @param email E-mail address of the recipient
	 * @param token E-mail change token
	 * @return
	 */
	@Async
	@Override
	public Boolean sendMailWithEmailChangeToken(
			@NotBlank @Email final String email,
			@NotBlank final String token
	) {
		Boolean result = Boolean.FALSE;
		logger.info("Called with e-mail {}, token {}", email, token);
		
		try {
			
			final MimeMessage message = javaMailSender.createMimeMessage();
			
			final MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setTo(email);
			helper.setSubject("Change e-mail");
			helper.setText("Change e-mail address, click the link below:<br />"
					+ "<a href='" + "https://localhost:8443" + "/api/v1/public/profile/settings/changeEmail/thanks?token=" + token + "'>" +
					"Click here to complete the change of your e-mail" +
					"</a>", true);
			
			sendMail(message);
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
			final JavaMailSenderImpl sender = new JavaMailSenderImpl();
			
			final MimeMessage message = sender.createMimeMessage();
			
			final MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setTo(email);
			helper.setSubject("Recover password");
			helper.setText("Your new password: " + "<b>" + newPassword + "</b>", true);
			
			sendMail(message);
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
