/**
 * 
 */
package com.c3trTranscibe.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c3trTranscibe.springboot.model.repository.RegisteredUser;

/**
 * @author rajesh
 *
 */
@RestController
public class LoginAuthController {

	private final Logger logger = LoggerFactory.getLogger(LoginAuthController.class);

	
	
	/**
	 * 
	 * 
	 */
	
	@RequestMapping(method = RequestMethod.POST, value = "/resetpwd" , produces = "application/jon")
public Map<String,String> resetPassword(@RequestBody RegisteredUser userDetails) {
	//statsd.incrementCounter(userHTTPPOST);
		
		final Map<String,String> res = new HashMap<>();
	logger.info("POST request : \"/reset\"");

	if (userDetails.getEmail() == null) {
		logger.error("Credentials should not be empty");
		res.put("RESPONSE", "User email not provided");
		return res;
	}

	logger.debug("Reset password for Email id request received : " + userDetails.getEmail());


		/*
		 * AmazonSNS snsClient = AmazonSNSClient.builder().withRegion("us-east-1")
		 * .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		 * AmazonSNS snsClient1 = AmazonSNSClient.builder().withRegion("us-east-1")
		 * .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		 */

	String resetEmail = userDetails.getEmail();
	String resetEmail1 = userDetails.getEmail();
	logger.info("Reset Email: " + resetEmail);
	logger.info("Reset Email: " + resetEmail1);

		/*
		 * PublishRequest publishRequest = new PublishRequest(topicArn,
		 * userDetails.getEmail()); PublishResult publishResult =
		 * snsClient.publish(publishRequest); PublishRequest publishRequest1 = new
		 * PublishRequest(topicArn, userDetails.getEmail()); PublishResult
		 * publishResult1 = snsClient.publish(publishRequest1);
		 */
	//TODO
	// password reset email service need to be have correct logic
	// as of now this only return true
	logger.info("password reset email sent " );
	
	res.put("RESPONSE", "Password Reset Link was sent to your emailID");
	
	return res;

}

public String checkAuth(String auth) {
	logger.info("Checking User's Basic Authentication");
	String[] encodedValue = auth.split(" ");
	logger.debug("auth Parameter : " + auth);
	String authValue = encodedValue[1];
	logger.debug("auth Value after split :" + authValue);
	byte[] decoded = Base64.decodeBase64(authValue.getBytes());

	String decodedValue = new String(decoded);
	logger.debug("Decoded Value:" + decodedValue);
	if (decodedValue.contains(":")) {
		String[] credentialValue;
		credentialValue = decodedValue.split(":");
		if (credentialValue.length < 2) {
			logger.error("Credentials should not be empty");
			return "{\"RESPONSE\" : \"Credentials should not be empty\"}";
		}
		email = credentialValue[0];
		password = credentialValue[1];
	} else {
		logger.info("User not registered, Please RegisteredUser");
		return "{\"RESPONSE\" : \"Please RegisteredUser\"}";
	}
	logger.debug("email : " + email + "/t" + "password : " + password);

	// check for empty strings
	if (email.isEmpty() || password.isEmpty()) {
		logger.error("Enter valid credentials");
		return "{\"RESPONSE\" : \"Enter valid Credentials\"}";
	}
	userDetails = new RegisteredUser(email, password);

	if (!checkVaildEmailAddr(email) || !checkAlreadyPresent(userDetails) || !checkPassword(userDetails)) {
		logger.error("Invalid credentials");
		return "{\"RESPONSE\" : \"Invalid credentials\"}";
	}
	return "Success";
}
}
