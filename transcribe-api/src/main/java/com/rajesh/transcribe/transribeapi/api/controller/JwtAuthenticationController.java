package com.rajesh.transcribe.transribeapi.api.controller;

import com.rajesh.transcribe.transribeapi.api.controller.exceptions.UserAlreadyRegisteredException;
import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import com.rajesh.transcribe.transribeapi.api.models.AppError;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationRequestDto;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationResponseDto;
import com.rajesh.transcribe.transribeapi.api.models.dto.RegisterUserDto;
import com.rajesh.transcribe.transribeapi.api.services.JwtUserDetailsService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.jfr.Registered;
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
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

	
	@ApiOperation(value = "Authenticate to api service", response = AuthenticationResponseDto.class, httpMethod = "POST")
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
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws Exception {
		final String email = authenticationRequestDto.getUsername();
		try {
			//byte[] decoded = java.util.Base64.getDecoder().decode();
			logger.debug("encode passwd:: {}", Base64.getDecoder().decode(authenticationRequestDto.getPassword().getBytes()));
			String hashedPassword = passwordEncoder.encode(authenticationRequestDto.getPassword());
			logger.debug("hashedpasswd:: {}", hashedPassword);
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()) );
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		catch (BadCredentialsException e) {
			logger.error("Incorrect username or password {}", authenticationRequestDto.getUsername());
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			AppError error = new AppError(HttpStatus.FORBIDDEN, "User [" + authenticationRequestDto.getUsername() + "] not found");
			return new ResponseEntity<AppError>(error, HttpStatus.FORBIDDEN);
			//throw new Exception("Incorrect username or password", e);
		}catch (AuthenticationException authEx){
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			
			throw new javax.naming.AuthenticationException("Server Error!");
		}


		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(authenticationRequestDto.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		AuthenticationResponseDto responseDto = new AuthenticationResponseDto(jwt,email, new Date(Instant.now().toEpochMilli()));
		responseDto.setEmail(null);

		return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Registers users", response = AuthenticationResponseDto.class, httpMethod = "POST")
	@ApiResponses(
			value = {
					@ApiResponse(code = 201, message = "Successfully created user."),
					@ApiResponse(code = 400, message = "Bad request, Please check your email."),
					@ApiResponse(
							code = 403,
							message = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
			})
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/jon")
	public ResponseEntity<Map<String,String>> registerUser(@RequestBody RegisterUserDto user, HttpServletRequest request, HttpServletResponse response) {
		// TODO handle session specific logic.
		Map<String, String> respMap = new ConcurrentHashMap();
		try {
			Boolean isUserCreated  = jwtUserDetailsService.registerUser(user);
		}catch (MessagingException ex){
			respMap.put("Error", "Error occured while sending email, Please check your email.");
			return  new ResponseEntity<>(respMap, HttpStatus.BAD_REQUEST);
		}catch (UserAlreadyRegisteredException ex){
			respMap.put("Error", "User is already registered, Please login!.");
			return  new ResponseEntity<>(respMap, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(respMap, HttpStatus.CREATED);
	}
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
	 * @param code
	 * @param emailId (email id in base64 encode form)
	 * @param req
	 * @param session
	 * @return
	 */
	@GetMapping(value = "/confirmEmail/{email}/{code}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, String> confirmEmail(
			@PathVariable("code") @NotNull @NotBlank final String code,
			@PathVariable("email") @NotNull @NotBlank final String emailId,
			HttpServletRequest req,
			HttpSession session) {
		String encryptedEmail = req.getParameter("emailId");
		String decodedEmail =
				new String(Base64.getUrlDecoder().decode(encryptedEmail), StandardCharsets.UTF_8);
		// TODO move this logic to service method and handle other use cases
		Map<String, String> resp = new HashMap<>();
		
		AppUsers user = jwtUserDetailsService.getUserByEmailAndInactive(decodedEmail);
		if(Objects.nonNull(user)) {
			boolean commandVerified = jwtUserDetailsService.activateUser(emailId, user);
			
			if (user.getEmail().equalsIgnoreCase(decodedEmail)) {
				boolean status = jwtUserDetailsService.activateUser(user.getEmail(), user);
			} else {
			
			}
			resp.put("token", session.getId());
			resp.put("message", "User is activated, Happy transcribing!.");
			resp.put("userName", user.getUsername());
			resp.put("email", user.getEmail());
		}else {
			RegisteredUserVerifyLogDetials registeredUserVerifyLogDetials = jwtUserDetailsService.getUserRegVerifyLogDetailsByEamil(emailId, code);
			if(Objects.nonNull(registeredUserVerifyLogDetials) )
			resp.put("email", emailId);
			resp.put("message", "Invalid code or email, Please register");
		}
		return resp;
	}

}
