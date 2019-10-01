/**
 * 
 */
package com.c3trTranscibe.springboot.controller;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c3trTranscibe.springboot.config.AppJwtTokenUtil;
import com.c3trTranscibe.springboot.domain.UserDTO;
import com.c3trTranscibe.springboot.model.JwtResponse;
import com.c3trTranscibe.springboot.model.repository.RegisteredUser;
import com.c3trTranscibe.springboot.services.JwtUserDetailsService;

/**
 * @author rajesh
 *
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AppJwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

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


	/**
	 * 
	 * @param auth
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/validateAuth" , produces = "application/jon")
	public Map<String,String> checkAuth(String auth,HttpServletRequest req) {

		Map<String,String> resp = new HashMap<>();
		
		logger.info("Checking User's Basic Authentication");
		
		String[] encodedValue = auth.split(" ");
		logger.debug("auth Parameter : " + auth);
		String authValue = encodedValue[1];
		logger.debug("auth Value after split :" + authValue);
		byte[] decoded = Base64.getDecoder().decode(authValue.getBytes());
		String email = null;
		String password = null;
		String decodedValue = new String(decoded);
		logger.debug("Decoded Value:" + decodedValue);
		if (decodedValue.contains(":")) {
			String[] credentialValue;
			credentialValue = decodedValue.split(":");
			if (credentialValue.length < 2) {
				logger.error("Credentials should not be empty");
				resp.put("RESPONSE","Credentials should not be empty");
				resp.put("status", HttpStatus.BAD_REQUEST.name());
				return resp;
			}
			email = credentialValue[0];
			password = credentialValue[1];
		} else {
			logger.info("User not registered, Please RegisteredUser");
			resp.put("status", HttpStatus.NON_AUTHORITATIVE_INFORMATION.name());
			resp.put("RESPONSE","Please RegisteredUser");
			return resp;
		}
		logger.debug("email : " + email + "/t" + "password : " + password);

		// check for empty strings
		if (email.isEmpty() || password.isEmpty()) {
			logger.error("Enter valid credentials");
			resp.put("RESPONSE","Enter valid Credentials");
			return resp;
		}
		RegisteredUser userDetails = new RegisteredUser(email, password);

		resp.put("status", HttpStatus.OK.name());
		resp.put("email", userDetails.getEmail());
		resp.put("user", userDetails.getID().toString());
		return resp;
	}
	
	
	@GetMapping(value = "/token",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public Map<String,String> token(HttpSession session) {
	    return Collections.singletonMap("token", session.getId());
	  }
}
