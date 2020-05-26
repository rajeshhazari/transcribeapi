package com.rajesh.transcribe.transribeapi.api.controller;

import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.model.dto.AppError;
import com.rajesh.transcribe.transribeapi.api.models.AuthenticationRequest;
import com.rajesh.transcribe.transribeapi.api.models.AuthenticationResponse;
import com.rajesh.transcribe.transribeapi.api.services.JwtUserDetailsService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@RestController
@CrossOrigin
@Api(
		value = "TranscriptionLoginController",
		description = "Authentication of users in to Transcription service")
public class JwtAuthenticationController {
	
	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	//TODO parameterize this
	private final Integer MAX_LOGIN_TRIES = 6;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	
	@ApiOperation(value = "Authenticate to api service", response = AuthenticationResponse.class, httpMethod = "POST")
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "Successfully Authenticated."),
					@ApiResponse(code = 401, message = "You are not authorized to login."),
					@ApiResponse(
							code = 403,
							message = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
			})
	@RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		final String email = authenticationRequest.getUsername();
		try {
			//byte[] decoded = java.util.Base64.getDecoder().decode();
			logger.debug("encode passwd:: {}", java.util.Base64.getDecoder().decode(authenticationRequest.getPassword().getBytes()));
			String hashedPassword = passwordEncoder.encode(authenticationRequest.getPassword());
			logger.debug("hashedpasswd:: {}", hashedPassword);
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()) );
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		catch (BadCredentialsException e) {
			logger.error("Incorrect username or password {}",authenticationRequest.getUsername());
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			AppError error = new AppError(HttpStatus.FORBIDDEN, "User [" + authenticationRequest.getUsername() + "] not found");
			return new ResponseEntity<AppError>(error, HttpStatus.FORBIDDEN);
			//throw new Exception("Incorrect username or password", e);
		}catch (AuthenticationException authEx){
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			
			throw new javax.naming.AuthenticationException("Server Error!");
		}


		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		

		return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt,email, new Date(Instant.now().toEpochMilli()),null), HttpStatus.OK);
	}
	/*
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/jon")
	public ResponseEntity<?> registerUser(@RequestBody AppUsers user) throws Exception {
		// TODO handle session specific logic.
		return ResponseEntity.ok(userDetailsService.save(user));
	}*/
	/*
	@RequestMapping(method = RequestMethod.POST, value = "/resetpwd", produces = "application/jon")
	public Map<String, String> resetPassword(@RequestBody RegisteredUser registeredUser) {
		// statsd.incrementCounter(userHTTPPOST);
		
		final Map<String, String> res = new HashMap<>();
		logger.info("POST request : "reset requested ");
		
		if (registeredUser.getEmail() == null) {
			logger.error("Credentials should not be empty");
			res.put("RESPONSE", "User email not provided");
			return res;
		}
		
		logger.debug("Reset password for Email id request received : " + registeredUser.getEmail());
		
		*//*
		 * AmazonSNS snsClient = AmazonSNSClient.builder().withRegion("us-east-1")
		 * .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		 * AmazonSNS snsClient1 = AmazonSNSClient.builder().withRegion("us-east-1")
		 * .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		 *//*
		
		String resetEmail = registeredUser.getEmail();
		String resetEmail1 = registeredUser.getEmail();
		logger.info("Reset Email: " + resetEmail);
		logger.info("Reset Email: " + resetEmail1);
		
		*//*
		 * PublishRequest publishRequest = new PublishRequest(topicArn,
		 * userDetails.getEmail()); PublishResult publishResult =
		 * snsClient.publish(publishRequest); PublishRequest publishRequest1 = new
		 * PublishRequest(topicArn, userDetails.getEmail()); PublishResult
		 * publishResult1 = snsClient.publish(publishRequest1);
		 *//*
		// TODO
		// password reset email service need to be have correct logic
		// as of now this only return true
		logger.info("password reset email sent ");
		
		res.put("RESPONSE", "Password Reset Link was sent to your emailID");
		
		return res;
	}*/

  
  
  /*
   * TODO: Need to check if this is required as we have jwt token auth working
   * * @param auth
   * @return
   *
   @RequestMapping(
      method = RequestMethod.POST,
      value = "/validateAuth",
      produces = "application/jon")
  public java.util.Map<String, String> checkAuth(ValidateAuthRequest auth, javax.servlet.http.HttpServletRequest req) {

    java.util.Map<String, String> resp = new java.util.HashMap<>();

    logger.info("Checking User's Basic Authentication");

    String[] encodedValue = auth.split(" ");
    logger.debug("auth Parameter : " + auth);
    String authValue = encodedValue[1];
    logger.debug("auth Value after split :" + authValue);
    byte[] decoded = java.util.Base64.getDecoder().decode(authValue.getBytes());
    String email = null;
    String password = null;
    String decodedValue = new String(decoded);
    logger.debug("Decoded Value:" + decodedValue);
    if (decodedValue.contains(":")) {
      String[] credentialValue;
      credentialValue = decodedValue.split(":");
      if (credentialValue.length < 2) {
        logger.error("Credentials should not be empty");
        resp.put("RESPONSE", "Credentials should not be empty");
        resp.put("status", org.springframework.http.HttpStatus.BAD_REQUEST.name());
        return resp;
      }
      email = credentialValue[0];
      password = credentialValue[1];
    } else {
      logger.info("User not registered, Please RegisteredUser");
      resp.put("status", org.springframework.http.HttpStatus.NON_AUTHORITATIVE_INFORMATION.name());
      resp.put("RESPONSE", "Please RegisteredUser");
      return resp;
    }
    logger.debug("email : " + email + "/t" + "password : " + password);

    // check for empty strings
    if (email.isEmpty() || password.isEmpty()) {
      logger.error("Enter valid credentials");
      resp.put("RESPONSE", "Enter valid Credentials");
      return resp;
    }
    AuthenticationRequest userDetails = new AuthenticationRequest();
	
    resp.put("status", org.springframework.http.HttpStatus.OK.name());
    resp.put("email", userDetails.getUsername());
    return resp;
  }
  */
	
	/**
	 *
	 * @param command (reset)
	 * @param emailId (email id in base64 encode form)
	 * @param req
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/confirmEmail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, String> confirmEmail(
			@RequestParam("command") @NotNull @NotBlank final String command,
			@RequestParam("emailId") @NotNull @NotBlank final String emailId,
			HttpServletRequest req,
			HttpSession session) {
		String encryptedEmail = req.getParameter("emailId");
		String decodedEmail =
				new String(Base64.getUrlDecoder().decode(encryptedEmail), StandardCharsets.UTF_8);
		// TODO move this logic to service method and handle other use cases
		Optional<AppUsers> user = jwtUserDetailsService.getUserByUsername(decodedEmail);
		Map<String, String> resp = new HashMap<>();
		
		if (user.get().getEmail().equalsIgnoreCase(decodedEmail)) {
			boolean status = jwtUserDetailsService.activateUser(user.get().getEmail(), user.get());
		} else {
		
		}
		resp.put("token", session.getId());
		resp.put("userName", user.get().getUsername());
		resp.put("email", user.get().getEmail());
		return resp;
	}

}
