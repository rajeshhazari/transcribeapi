/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import com.c3trTranscibe.springboot.repository.UsersRepository;
import com.c3trTranscibe.springboot.repository.exceptions.UserNotFoundException;
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
	private UsersRepository usersRepo;
	
	private String email;
	private String password;
	public RegisteredUser userDetails;
	public Users user;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> user = usersRepo.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		};
		
		/*
		 * List<Users> userList = userRepo.findByUsername(username); 
		 * Users userresp = null; 
		 * for (Users user : userList) { 
		 * userresp = user; 
		 * if (user == null) {
		 * throw new UsernameNotFoundException("User not found with username: " +
		 * username); } }
		 */
		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
				new ArrayList<>());
	}


	public Optional<Users> getUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> users = usersRepo.findByUsername(username);

		if (users == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		};

		/*
		 * List<Users> userList = userRepo.findByUsername(username);
		 * Users userresp = null;
		 * for (Users user : userList) {
		 * userresp = user;
		 * if (user == null) {
		 * throw new UsernameNotFoundException("User not found with username: " +
		 * username); } }
		 */
		return users;
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 * @throws UserNotFoundException
	 */
	public Users getUserByEmail(String email) throws UserNotFoundException {
		Optional<Users> user = usersRepo.findByEmailAndInactive(email);
		
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		};
		return user.get();
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
		return usersRepo.save(newUser);
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

		if ( !checkAlreadyPresent(userDetails) || !checkPassword(userDetails)) {
			logger.error("Invalid credentials");
			return "{\"RESPONSE\" : \"Invalid credentials\"}";
		}
		return "Success";
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
		
		

	public Users checkUserAlreadyPresent(RegisteredUser userDetails) {
		logger.info("Checking Email ID Already present");
		List<Users> userList = usersRepo.findByEmailAndusernameAndActive(userDetails.getUsername(),userDetails.getEmail());
		Optional<Users> userVal = Optional.empty();
		for (Users user : userList) {
			if (Objects.nonNull(user) && user.isActive() && userDetails.getEmail().equalsIgnoreCase(user.getEmail())) {
				logger.debug("{}   : user already registered ", userDetails.getEmail());
				return user;
			}
			if (Objects.nonNull(user) && user.isDisabled() && userDetails.getEmail().equalsIgnoreCase(user.getEmail())) {
				logger.debug("{}  :  user already registered and Disabled", userDetails.getEmail());
				return user;
			}
			if (Objects.nonNull(user) && user.isLocked() && userDetails.getEmail().equalsIgnoreCase(user.getEmail())) {
				logger.debug("{}  :  user already registered and id is locked.", userDetails.getEmail());
				return user;
			}
			if (Objects.nonNull(user) && user.isLocked() && userDetails.getEmail().equalsIgnoreCase(user.getEmail())) {
				logger.debug("{}  :  user already registered and id is locked.", userDetails.getEmail());
				return user;
			}
		}
		return userVal.get();
	}
	
	
	
	/**
	 * 
	 * @param userDetails
	 * @return
	 */
	public boolean checkPassword(RegisteredUser userDetails) {
		logger.info("Checking password");
		logger.debug(" checking password for user :: %s",userDetails.getEmail());
		Optional<Users> user = usersRepo.findByUsername(userDetails.getUsername());
		
			if (Objects.nonNull(user) && user.isPresent()) {
				// String password = BCrypt.hashpw(userDetails.getPassword(), BCrypt.gensalt());
				//TODO add password validation in db after changing the password as encripted value from request
				if (BCrypt.checkpw(userDetails.getPassword(), user.get().getEmail())) {
					logger.debug("password  matches");
					return true;
				} else {
					logger.error("password does not match");
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
		userData.setActive(false);
		//TODO generate user confirmation link  

		return Objects.nonNull(usersRepo.save(userData));
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

