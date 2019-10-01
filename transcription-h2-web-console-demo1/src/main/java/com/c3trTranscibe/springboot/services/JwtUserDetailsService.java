/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.domain.UserDTO;
import com.c3trTranscibe.springboot.domain.Users;
import com.c3trTranscibe.springboot.model.repository.RegisteredUser;
import com.c3trTranscibe.springboot.repository.UserRepository;
/**
 * @author rajesh
 *
 */

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	//TODO: Change this class to use jdbc repo class
	//TODO: either postgress or h2 db
	private Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
	
	
	@Autowired
	Environment env;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	
	@Autowired
	private UserRepository userRepo;
	
	private String email;
	private String password;
	public RegisteredUser userDetails;
	public Users user;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public Users save(UserDTO user) {
		Users newUser = new Users();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepo.save(newUser);
	}
		
		
		/**
		 * 
		 * 
		 * @RequestMapping(method = RequestMethod.POST, value = "/resetdemo")
	public String reserPassword(@RequestBody RegisteredUser userDetails) {
		statsd.incrementCounter(userHTTPPOST);

		logger.info("POST request : \"/reset\"");

		if (userDetails.getEmail() == null) {
			logger.error("Credentials should not be empty");
			return "{\"RESPONSE\" : \"User email not provided\"}";
		}

		logger.debug("Reset password for Email id : " + userDetails.getEmail());

		if (checkVaildEmailAddr(userDetails.getEmail())) {
			if (!checkAlreadyPresent(userDetails)) {
				logger.debug("This user is not registered with us");
				return "{\"RESPONSE\" : \"User not registered ! Please RegisteredUser\"}";
			}
		}

		AmazonSNS snsClient = AmazonSNSClient.builder().withRegion("us-east-1")
				.withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		AmazonSNS snsClient1 = AmazonSNSClient.builder().withRegion("us-east-1")
				.withCredentials(new InstanceProfileCredentialsProvider(false)).build();

		String resetEmail = userDetails.getEmail();
		String resetEmail1 = userDetails.getEmail();
		logger.info("Reset Email: " + resetEmail);
		logger.info("Reset Email: " + resetEmail1);

		PublishRequest publishRequest = new PublishRequest(topicArn, userDetails.getEmail());
		PublishResult publishResult = snsClient.publish(publishRequest);
		PublishRequest publishRequest1 = new PublishRequest(topicArn, userDetails.getEmail());
		PublishResult publishResult1 = snsClient.publish(publishRequest1);
		logger.info("SNS Publish Result: " + publishResult);

		return "{\"RESPONSE\" : \"Password Reset Link was sent to your emailID\"}";

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

	public boolean checkVaildEmailAddr(String email) {
		logger.info("Checking Email ID pattern");
		Matcher mat = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return mat.find();
	}

	public boolean checkAlreadyPresent(RegisteredUser userDetails) {
		logger.info("Checking Email ID Already present");
		ArrayList<RegisteredUser> dbList = new ArrayList<>(userRepository.findAll());

		for (RegisteredUser i : dbList) {
			if (i.getEmail().equals(userDetails.getEmail())) {
				logger.debug("already registered");
				return true;
			}
		}
		return false;
	}

	public boolean checkPassword(RegisteredUser userDetails) {
		logger.info("Checking password");
		ArrayList<RegisteredUser> dbList = new ArrayList<>(userRepository.findAll());

		for (RegisteredUser i : dbList) {
			if (i.getEmail().equals(userDetails.getEmail())) {
				// String password = BCrypt.hashpw(userDetails.getPassword(), BCrypt.gensalt());
				if (BCrypt.checkpw(userDetails.getPassword(), i.getPassword())) {
					logger.debug("It matches");
					return true;
				} else
					logger.error("It does not match");
			}
		}
		return false;
	}

	


		 */
		
		
		
	
	
	
	/**
	 * 
	 * @param userDetails
	 * @return
	 */
	public boolean checkPassword(RegisteredUser userDetails) {
		logger.info("Checking password");
		logger.debug(" checking password for user :: %s",userDetails.getEmail());
		ArrayList<Users> dbList = new ArrayList<>((Collection<? extends Users>) userRepo.findAll());

		for (Users user : dbList) {
			if (user.getEmailAddress().equals(userDetails.getEmail())) {
				// String password = BCrypt.hashpw(userDetails.getPassword(), BCrypt.gensalt());
				//TODO add passowrd to the below if
				if (BCrypt.checkpw(userDetails.getPassword(), user.getEmailAddress())) {
					logger.debug("It matches");
					return true;
				} else
					logger.error("It does not match");
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param userData
	 * @return
	 */
	public boolean registerUser(final Users userData) {

		String password = BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt());
		logger.debug("Password salt : " + password);
		userData.setPassword(password);

		userRepo.save(userData);
		return true;
	}
		
	
	/**
	 * 
	 * @param password
	 * @return
	 */
	public boolean isValidPassword(String password) {
		if (!(password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"))) {
			logger.error("Invalid password");
			return false;
		}
		logger.debug("Valid password");
		return true;
	}
	
}

