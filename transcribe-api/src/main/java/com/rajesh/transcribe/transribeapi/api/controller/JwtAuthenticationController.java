package com.rajesh.transcribe.transribeapi.api.controller;

import com.c3transcribe.core.utils.EncryptUtils;
import com.rajesh.transcribe.transribeapi.api.controller.exceptions.UserAlreadyRegisteredException;
import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.models.AppError;
import com.rajesh.transcribe.transribeapi.api.models.dto.*;
import com.rajesh.transcribe.transribeapi.api.repository.RegisteredUsersRepo;
import com.rajesh.transcribe.transribeapi.api.repository.exceptions.UserNotFoundException;
import com.rajesh.transcribe.transribeapi.api.services.AppEmailServiceImpl;
import com.rajesh.transcribe.transribeapi.api.services.JwtUserDetailsService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
@Api(
		value = "TranscriptionLoginController",
		description = "Authentication of users in to Transcription service")
public class JwtAuthenticationController {
	
	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	//TODO parameterize this
	private final Integer MAX_LOGIN_TRIES = 6;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtTokenUtil;
	private JwtUserDetailsService jwtUserDetailsService;
	private RegisteredUsersRepo registeredUserRepo;
	private EncryptUtils encryptUtils;
	private AppEmailServiceImpl appEmailService;
	private ModelMapper modelMapper;

	@Value("${app.io.bufferSize}")
	private String appMaxUploadLimit;

	public JwtAuthenticationController( AuthenticationManager authenticationManager,
									    PasswordEncoder passwordEncoder,
									    JwtUtil jwtTokenUtil,
										JwtUserDetailsService jwtUserDetailsService,  RegisteredUsersRepo registeredUserRepo,
										AppEmailServiceImpl appEmailService, EncryptUtils encryptUtils,
										ModelMapper modelMapper ) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.registeredUserRepo = registeredUserRepo;
		this.appEmailService = appEmailService;
		this.encryptUtils = encryptUtils;
		this.modelMapper = modelMapper;
	}
	
	@Timed ( description = "createAuthenticationToken")
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
	public ResponseEntity<AuthenticationResponseDto> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto,
													   HttpServletRequest request,
													   HttpServletResponse response) throws Exception {
		final String email = authenticationRequestDto.getUsername();
		AuthenticationResponseDto responseDto = null;
		AuthUserProfileDto authUserProfileDto = null;
		StringBuilder loginErrorMsg = new StringBuilder("Incorrect username or password : %s");
		/*Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(auth);*/
		UserDetails userdetails ;
		try {
			jwtUserDetailsService.authenticate(email, authenticationRequestDto.getPassword());
			userdetails = jwtUserDetailsService.loadUserByUsername(email,authenticationRequestDto.getPassword());
			if(Objects.isNull(userdetails) ){
				loginErrorMsg = new StringBuilder("Bypassed filtered and Unregistered User tried to login/OR could be an bot ::");
				loginErrorMsg.append(email);
				logger.debug(loginErrorMsg.toString());
				throw new UserNotFoundException(loginErrorMsg.toString());
			}else {
				authUserProfileDto = jwtUserDetailsService.getUserDto(email);
				logger.info("User retrieved from DB {} ::", authUserProfileDto.getEmail());
				if(!authUserProfileDto.isActive()){
					throw new BadCredentialsException("Registered User tried to login email before activating.");
				}else if(authUserProfileDto.isActive() && authUserProfileDto.isLocked()){
					logger.debug("User tried to login email, but email is marked as locked {} ::", authUserProfileDto.getEmail());
					throw new BadCredentialsException("User tried to login email, but email is marked as locked");
				}else if(!authUserProfileDto.isActive() && authUserProfileDto.isLocked()){
					logger.debug("User tried to login email, but email is marked as locked {} ::", authUserProfileDto.getEmail());
					throw new BadCredentialsException("User tried to login email, but email is marked as locked");
				}else if (authUserProfileDto.isVerified() && authUserProfileDto.isActive() && !authUserProfileDto.isLocked()) {
					//logger.debug("encode passwd:: {}", Base64.getDecoder().decode(authenticationRequestDto.getPassword().getBytes()));
					String passwordHash = passwordEncoder.encode(authenticationRequestDto.getPassword());
					logger.debug("hash passwd:: {}", passwordHash);
					
				} else if (!authUserProfileDto.isVerified()) {
					logger.debug("User tried to login before verifying email {} ::", authUserProfileDto.getEmail());
					throw new BadCredentialsException("User tried to login before verifying email. ");
					
				} else if (authUserProfileDto.isLocked()) {
					logger.debug("Bypassed filtered and Unregistered User tried to login/OR could be an bot {} ::", email);
					throw new BadCredentialsException("Bypassed filtered and Unregistered User tried to login/OR could be an bot ");
					
				} else {
					logger.debug("Last else:  Unhandled use case : User tried to login before verifying email {} ::", email);
				}
			}
		}catch (BadCredentialsException e) {
			logger.error("Incorrect username or password {}, exception message :: {}", authenticationRequestDto.getUsername(), e.getMessage());
			loginErrorMsg = new StringBuilder(e.getMessage());
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			AppError error = new AppError(HttpStatus.UNAUTHORIZED, String.format(loginErrorMsg.toString(), authenticationRequestDto.getUsername()));
			responseDto = new AuthenticationResponseDto();
			responseDto.setError(error);
			return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.UNAUTHORIZED);
		}catch (AuthenticationException authEx){
			//TODO save login tries to DB
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			authEx.printStackTrace();
			if(!jwtUserDetailsService.updateLoginTries(email)){
				logger.error("unable to save user retries in db for email :: {}  ", email);
			}
			AppError error = new AppError(HttpStatus.BAD_REQUEST, authEx.getMessage());
			responseDto = new AuthenticationResponseDto();
			responseDto.setError(error);
			return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.BAD_REQUEST);
		}catch (BadSqlGrammarException badSqlGex){
			// Check login retries with value and if retries >= MAX_LOGIN_TRIES  update the user to lock
			logger.error("unable to authenticate used in db for email :: {}  ", email);
			AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServer Error please try again, after some time!. ");
			responseDto = new AuthenticationResponseDto();
			responseDto.setError(error);
			return new ResponseEntity<AuthenticationResponseDto>(responseDto, HttpStatus.BAD_REQUEST);
		}

		//Add the fingerprint in a hardened cookie - Add cookie manually because
		//SameSite attribute is not supported by javax.servlet.http.Cookie class
		String userFingerprint = encryptUtils.randomToken();
		final String jwt = jwtTokenUtil.generateJwtToken(userdetails,userFingerprint);
		String fingerprintCookie = "__Secure-Fgp=" + userFingerprint
				+ "; SameSite=Strict; HttpOnly; Secure";
		response.addHeader("Set-Cookie", fingerprintCookie);
		
		responseDto = new AuthenticationResponseDto(jwt,email, new Date(Instant.now().toEpochMilli()));
		responseDto.setEmail(authenticationRequestDto.getUsername());
		
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
	
	@RequestMapping(value = "/public/register", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<UserRegReqResponseDto> registerUser(@RequestBody RegisterUserRequest registerUserRequest, HttpServletRequest request, HttpServletResponse response) {
		// TODO handle session specific logic.
		UserRegReqResponseDto userRegReqResponseDto = new UserRegReqResponseDto();
		try {
			userRegReqResponseDto.setEmail(registerUserRequest.getEmail());
			userRegReqResponseDto = jwtUserDetailsService.registerUser(registerUserRequest);
			/*
			AppUsers users = new AppUsers();
			jwtUserDetailsService.save(users);
			appEmailService.sendMailWithUsername(registerUserRequest.getEmail(), registerUserRequest.getLastName());*/
		/*}catch (MessagingException ex){
			respMap.put("Error", "Error occured while sending email, Please check your email.");
			return  new ResponseEntity<>(respMap, HttpStatus.BAD_REQUEST);
			*/
		}catch (UserAlreadyRegisteredException ex){
			userRegReqResponseDto.setErrorMessage("User is already registered, Please login!.");
			return  new ResponseEntity<>(userRegReqResponseDto, HttpStatus.BAD_REQUEST);
		} catch (MessagingException ex) {
			userRegReqResponseDto.setErrorMessage("User is registered, System was unable to send verification email, Please check your email!.");
			ex.printStackTrace();
		}catch (Exception ex){
			userRegReqResponseDto.setErrorMessage("Error occured while sending email, Please check your email.");
			return  new ResponseEntity<>(userRegReqResponseDto, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(userRegReqResponseDto, HttpStatus.CREATED);
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

	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			String jwt = jwtTokenUtil.extractUsername(auth.getPrincipal().toString());
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		
	}
	
	
	 Cookie createDomainCookie(String name, String value, int maxAgeInMinutes) {
		ZonedDateTime time = ZonedDateTime.now().plusMinutes(maxAgeInMinutes);
		long expiry = time.toInstant().toEpochMilli();
		Cookie newCookie = new Cookie(name, value);
				newCookie.setDomain("/");
				newCookie.setHttpOnly(true);
				newCookie.setSecure(true);
				newCookie.setMaxAge(maxAgeInMinutes*60);
		return newCookie;
	}

	@ApiOperation(value = "Request for to get userprofile .", response = AuthUserProfileDto.class, httpMethod = "GET")
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "Successfully Send User profile."),
					@ApiResponse(code = 401, message = "You are not authorized to encrypt."),
					@ApiResponse(
							code = 403,
							message = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
			})
	@GetMapping( value = "/profile",produces = "application/json" )
	public  ResponseEntity <AuthUserProfileDto> getUserProfile(@RequestParam("email") @Validated String email,
															   HttpServletRequest request, HttpServletResponse response){
		//TODO handle error scenarios
		AuthUserProfileDto authUserProfileDto = jwtUserDetailsService.getUserDto(email);
		HttpStatus status = authUserProfileDto != null ?
				HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<AuthUserProfileDto>(authUserProfileDto, status);
	}
	
	
	@ApiOperation(value = "Request for to update user profile .", response = Boolean.class, httpMethod = "POST")
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "Successfully Send User profile."),
					@ApiResponse(code = 401, message = "You are not authorized to encrypt."),
					@ApiResponse(
							code = 403,
							message = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
			})
	@PostMapping(value = "/profile" ,produces = "application/json"  )
	public ResponseEntity<Boolean> updateProfile(@RequestParam("email") @Validated String email,
								HttpServletRequest request, HttpServletResponse response,
			@RequestAttribute AuthUserProfileDto authUserProfileDto ) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User authorizedUser = (User) authentication.getPrincipal();
		AppUsers appUsers = new AppUsers();
		BeanUtils.copyProperties(authUserProfileDto, appUsers);
		Boolean updateStatus = jwtUserDetailsService.save(appUsers) != null ? Boolean.TRUE : Boolean.FALSE;
		HttpStatus status = authUserProfileDto != null ?
				HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Boolean>(updateStatus, status);
	}
}
