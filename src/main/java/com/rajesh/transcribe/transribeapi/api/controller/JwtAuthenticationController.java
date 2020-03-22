package com.rajesh.transcribe.transribeapi.api.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(
		value = "TranscriptionLoginController",
		description = "Authentication of users in to Transcription service")
public class JwtAuthenticationController {
	
	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	
	@ApiOperation(value = "Authenticate to api service", response = AuthenticationResponse.class, httpMethod = "HttpMethod.POST")
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

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
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
		logger.info("POST request : \"/reset\"");
		
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
   * @param auth
   * @return
   *
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/validateAuth",
      produces = "application/jon")
  public Map<String, String> checkAuth(String auth, HttpServletRequest req) {

    Map<String, String> resp = new HashMap<>();

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
        resp.put("RESPONSE", "Credentials should not be empty");
        resp.put("status", HttpStatus.BAD_REQUEST.name());
        return resp;
      }
      email = credentialValue[0];
      password = credentialValue[1];
    } else {
      logger.info("User not registered, Please RegisteredUser");
      resp.put("status", HttpStatus.NON_AUTHORITATIVE_INFORMATION.name());
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
    RegisteredUser userDetails = new RegisteredUser();

    resp.put("status", HttpStatus.OK.name());
    resp.put("email", userDetails.getEmail());
    resp.put("user", userDetails.getID().toString());
    return resp;
  }*/
	
	/*@GetMapping(value = "/confirmEmail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, String> confirmEmail(
			@RequestParam("command") @NotNull @NotBlank final String command,
			@RequestParam("emailId") @NotNull @NotBlank final String emailId,
			HttpServletRequest req,
			HttpSession session) {
		String encryptedEmail = req.getParameter("emailId");
		String decodedEmail =
				new String(Base64.getUrlDecoder().decode(encryptedEmail), StandardCharsets.UTF_8);
		// TODO add a new methos to get user with his email id
		Optional<AppUsers> user = userDetailsService.getUserByUsername(decodedEmail);
		Map<String, String> resp = new HashMap<>();
		
		if (user.get().getEmail().equalsIgnoreCase(decodedEmail)) {
			user.get().setActive(true);
		}
		resp.put("token", session.getId());
		resp.put("userName", user.get().getUsername());
		return resp;
	}*/

}