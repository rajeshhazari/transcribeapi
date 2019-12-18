/**
 * 
 */
package com.c3trTranscibe.springboot.services.email;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author rajesh
 *
 */

@Service("emailService")
public class EmailService {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
	private final static String confirmationURL = "http://localhost:8585/api/v1/confirmEmail?command=confirmRegistration&ID=";
	
	@Autowired
	Environment env;

	@Autowired
    private JavaMailSender mailSender;
      
    @Autowired
    private SimpleMailMessage preConfiguredMessage;
  
    /**
     * This method will send compose and send the message 
     * */
    public void sendMail(String to, String subject, String body) 
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        //mailSender.send(message);
    }
  
    /**
     * This method will send a pre-configured message
     * */
    public void sendPreConfiguredMail(String message) 
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        mailMessage.setText(message);
        //mailSender.send(mailMessage);
    }
    
    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach) 
    {
		/*
		 * MimeMessagePreparator preparator = new MimeMessagePreparator() { public void
		 * prepare(MimeMessage mimeMessage) throws Exception {
		 * mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		 * mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
		 * mimeMessage.setSubject(subject); mimeMessage.setText(body);
		 * 
		 * FileSystemResource file = new FileSystemResource(new File(fileToAttach));
		 * MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		 * helper.addAttachment("logo.jpg", file); } };
		 * 
		 * try { mailSender.send(preparator); } catch (MailException ex) { // simply log
		 * it and go on... LOGGER.error(ex.getMessage()); }
		 */
    }
    
    
    public void sendMailWithInlineResources(String to, String subject, String fileToAttach) 
    {
		/*
		 * MimeMessagePreparator preparator = new MimeMessagePreparator() { public void
		 * prepare(MimeMessage mimeMessage) throws Exception {
		 * mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));confirmationURL
		 * mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
		 * mimeMessage.setSubject(subject);
		 * 
		 * MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		 * 
		 * helper.setText("<html><body><img src='cid:identifier1234'></body></html>",
		 * true);
		 * 
		 * FileSystemResource res = new FileSystemResource(new File(fileToAttach));
		 * helper.addInline("identifier1234", res); } };
		 * 
		 * try { mailSender.send(preparator); } catch (MailException ex) { // simply log
		 * it and go on... LOGGER.error(ex.getMessage()); }
		 */
    }
    
    public void sendConfirmationEmail(String email) throws UnsupportedEncodingException, MessagingException {
	byte[] encodedEmail = Base64.getUrlEncoder().encode(email.getBytes(StandardCharsets.UTF_8));
	Multipart multipart = new MimeMultipart();
	InternetHeaders headers = new InternetHeaders();
	headers.addHeader("Content-type", "text/html; charset=UTF-8");
	String confirmLink = "Complete your registration by clicking on following"+ "\n<a href='" + env.getProperty("confirmationURL", confirmationURL) + encodedEmail + "'>link</a>";
	MimeBodyPart link = new MimeBodyPart(headers,	confirmLink.getBytes("UTF-8"));
	multipart.addBodyPart(link);
    }
    
}
