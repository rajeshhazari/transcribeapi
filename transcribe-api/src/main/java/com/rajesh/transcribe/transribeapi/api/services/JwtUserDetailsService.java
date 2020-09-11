package com.rajesh.transcribe.transribeapi.api.services;

import com.rajesh.transcribe.transribeapi.api.controller.exceptions.UserAlreadyRegisteredException;
import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.domian.AppUsersAuth;
import com.rajesh.transcribe.transribeapi.api.domian.AuthoritiesMaster;
import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthUserProfileDto;
import com.rajesh.transcribe.transribeapi.api.models.dto.RegisterUserRequest;
import com.rajesh.transcribe.transribeapi.api.models.dto.UserRegReqResponseDto;
import com.rajesh.transcribe.transribeapi.api.repository.AppUsersRepository;
import com.rajesh.transcribe.transribeapi.api.repository.AuthoritiesMasterRepo;
import com.rajesh.transcribe.transribeapi.api.repository.RegisteredUsersRepo;
import com.rajesh.transcribe.transribeapi.api.repository.exceptions.UserNotFoundException;
import com.rajesh.transcribe.transribeapi.api.util.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

import static com.rajesh.transcribe.transribeapi.api.util.SystemConstants.*;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    
    private Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    
    @Autowired Environment env;
    @Autowired private PasswordEncoder bcryptEncoder;
    @Autowired private AppUsersRepository userRepo;
    @Autowired private RegisteredUsersRepo registeredUsersRepo;
    @Autowired private AppMailService appEmailServiceImpl;
    @Autowired private DataSourceHealthIndicator dataSourceHealthIndicator;
    @Autowired private SecureRandom secureRandom;
    @Autowired private AuthoritiesMasterRepo authoritiesMasterRepo;

    /*@Autowired
    public JwtUserDetailsService (PasswordEncoder bcryptEncoder,
                                  AppUsersRepository userRepo,
                                  RegisteredUsersRepo registeredUsersRepo,
                                  AppMailService appEmailService,
                                  DataSourceHealthIndicator dataSourceHealthIndicator,
                                  SecureRandom secureRandom){
        this.bcryptEncoder = bcryptEncoder;
        this.userRepo = userRepo;
        this.registeredUsersRepo = registeredUsersRepo;
        this.appEmailServiceImpl = appEmailService;
        this.dataSourceHealthIndicator = dataSourceHealthIndicator;
        this.secureRandom = secureRandom;
    }*/
    
    /*@Autowired
    private SessionRegistry sessionRegistry;*/

    @PostConstruct
    public void init(){
     Health dbHealth = dataSourceHealthIndicator.getHealth(true);
    }


    /**
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //List<AppUsers> appuser = userRepo.findByEmail(s);
        Optional<AppUsers> user = null;
        try{
            user = Optional.ofNullable(userRepo.findByEmail(email));
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }
        logger.debug("user retrived:: %s  for user %s",user ,email );
    
        if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + email);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is locker. Please Contact us for more info: " + email);
        }
    
    
        /*
         * List<Users> userList = userRepo.findByUsername(username);
         * Users userresp = null;
         * for (Users user : userList) {
         * userresp = user;
         * if (user == null) {
         * throw new UsernameNotFoundException("User not found with username: " +
         * username); } }
         */
        //Principal authentication = new HttpTrace.Principal()
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.get().getEmail(),user.get().getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        context.setAuthentication(authentication);
        return new User(user.get().getEmail(), user.get().getPassword(),
                new ArrayList<>());
        /*return new User("admin", "admin321",
                new ArrayList<>());*/
    }
    
    /**
     *
     * @param email
     * @return
     */
    private AuthUserProfileDto getUserDto(String email) throws UsernameNotFoundException {
        Optional<AppUsers> user = null;
        AuthUserProfileDto userDto = new AuthUserProfileDto();
        try{
            user = Optional.ofNullable(userRepo.findByEmail(email));
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }
        logger.debug("user retrived:: {}  for user {}",user ,email );
        if (!user.isPresent() || user.isPresent()) {
            userDto.setUsername(user.get().getEmail());
            userDto.setActive(user.get().isActive());
            userDto.setLocked(user.get().isLocked());
            userDto.setEnabled(user.get().isDisabled());
            userDto.setLastLoggedin(user.get().getLastModified());
        }
        if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + email);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is lockerd Please Contact us for more info: " + email);
        }
        
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.get().getEmail(),user.get().getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        context.setAuthentication(authentication);
        return userDto;
        
    }
    
    /**
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public Optional<AppUsers> getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUsers> user = userRepo.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
        
        }
        return user;
    }
    
    /**
     *
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    public AppUsers getUserByEmailAndInactive(String email) throws UserNotFoundException {
        Optional<AppUsers> user = userRepo.findByEmailAndInactive(email);
        
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user.get();
    }
    
    /**
     *
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    public AppUsers getUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUsers> user = Optional.ofNullable(userRepo.findByEmail(email));
        
        if (user.isEmpty() || !user.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + email);
        };
        return user.get();
    }
    
    /**
     *
     * @param regUser
     * @return
     */
    public AppUsers save(AppUsers regUser) {
        regUser.setPassword(bcryptEncoder.encode(regUser.getPassword()));
        
        logger.debug("New User added - username:: {} adn email:: ",regUser.getUsername() , regUser.getEmail());
        return userRepo.save(regUser);
    }
    
    /**
     *
     * TODO: Handle other use cases
     *
     * @param email
     * @param appUser
     * @return
     */
    public boolean activateUser(final String email, AppUsers appUser) {
        appUser.setActive(true);
        appUser =  userRepo.save(appUser);
        if(appUser.isLocked()){
            return false;
        }
        return true;
    }
    
    
    public AppUsers getCurrentUser() {
        return getUserByEmail(((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername());
    }
    
    /**
     *
     * @param registerUserRequest
     * @return
     */
    public UserRegReqResponseDto registerUser(final RegisterUserRequest registerUserRequest) throws MessagingException {
        
        UserRegReqResponseDto userRegReqResponseDto = new UserRegReqResponseDto();
        final String emailRegReq = registerUserRequest.getEmail();
        userRegReqResponseDto.setEmail(emailRegReq);
        try{
            Optional<AppUsers> users = Optional.ofNullable(userRepo.findByEmail(emailRegReq));
    
            if( isEmailRegistered(emailRegReq) && users.isPresent() && users.get().isVerified()){
                userRegReqResponseDto.setEmail(emailRegReq);
                userRegReqResponseDto.setAlreadyRegistered(true);
                userRegReqResponseDto.setDisabledEmail(users.get().isDisabled());
                userRegReqResponseDto.setVerificationPending(users.get().isVerified());
                userRegReqResponseDto.setErrorMessage(REGISTERED_AND_VERIFIED_EMAIL_SYSTEM_ERROR_MSG);
                return userRegReqResponseDto;
            }else if( isEmailRegistered(emailRegReq) && !users.isPresent() && !users.get().isVerified()) {
                logger.debug("verifying if email is registered and not verified %s", emailRegReq);
                if(isRegisteredEmailVerified(emailRegReq)){
                    userRegReqResponseDto.setErrorMessage(REGISTERED_EMAIL_PENDING_VERIFICATION_SYSTEM_ERROR_MSG);
                    userRegReqResponseDto.setVerificationPending(true);
                }
            } else if( !isEmailRegistered(emailRegReq)){
                logger.debug("Email is not registered %s, registering the user", emailRegReq);
                AppUsers appUsers = new AppUsers();
                appUsers.setEmail(emailRegReq);
                BeanUtils.copyProperties(registerUserRequest, appUsers);
                userRepo.save(appUsers);
                RegisteredUserVerifyLogDetials newUser = new RegisteredUserVerifyLogDetials();
                newUser.setEmail(emailRegReq);
                newUser.setUsername(registerUserRequest.getUsername());
                newUser = registeredUsersRepo.save(newUser);
                appEmailServiceImpl.sendMailWithEmailChangeToken( emailRegReq,null);
            } else {
                userRegReqResponseDto.setErrorMessage(USER_REGISITRATION_SYSTEM_ERROR_MSG);
                logger.debug(USER_REGISITRATION_SYSTEM_ERROR_MSG + " %s ", emailRegReq);
            }
            
        }catch (DuplicateKeyException ex){
            userRegReqResponseDto.setErrorMessage("User Already register with email!");
            if ( isRegisteredEmailVerified(emailRegReq)){
                userRegReqResponseDto.setVerificationPending(true);
            }
            throw new UserAlreadyRegisteredException("User Already register with email"+registerUserRequest.email);
        }catch (DataAccessResourceFailureException ex){
            userRegReqResponseDto.setErrorMessage(SystemConstants.DB_UNAVAILABLE_MSG);
            throw  new ServerErrorException(SystemConstants.DB_UNAVAILABLE_MSG, ex);
        }
        
       return userRegReqResponseDto;
    }
    
    /**
     * 
     * @param email
     * @param code
     * @return
     */
    public Boolean verifyEmailForUserActivation(final String email, final String code) {
        Boolean result = false;
        if(!isEmailRegistered(email)){
            logger.debug("Suspicious Alert:: Wrong email or wrong token manipulation email:: %s token :: %s ", email, code);
            result =  false;
        }
        List<RegisteredUserVerifyLogDetials> registeredUserVerifyLogDetialsList =  registeredUsersRepo.findByEmailAndCode(email, Integer.parseInt(code));
        RegisteredUserVerifyLogDetials registeredUserVerifyLogDetials = registeredUserVerifyLogDetialsList.stream().filter(user -> user.isVerified()).findFirst().get();
        if(registeredUserVerifyLogDetials.isVerificationEmailSent() && registeredUserVerifyLogDetials.isVerified()){
            result = true;
        } else if(registeredUserVerifyLogDetials.isVerificationEmailSent() && !registeredUserVerifyLogDetials.isVerified()){
            logger.warn("Email is is already verified.{}", email);
            //TODO handle other usecase  like multiple submission or more.
            return true;
        }
        return  false;
    }
    
    /**
     * This method will validate if the given email is registerd and verified.
     * @param email
     * @return
     */
    public boolean isRegisteredEmailVerified(String email){
        boolean isVerified = false;
        if(!isEmailRegistered(email)){
            return false;
        } else {
            List<RegisteredUserVerifyLogDetials> registeredUserVerifyLogDetialsList = registeredUsersRepo.findByEmail(email);
            if (!registeredUserVerifyLogDetialsList.isEmpty()) {
                RegisteredUserVerifyLogDetials regUser = registeredUserVerifyLogDetialsList.stream().findFirst().get();
                isVerified = regUser.isVerified();
            } else {
                isVerified = false;
            }
        }
        return isVerified;
    }
    
    /**
     * This method will validate if the given email is registered.
     * @param email
     * @return
     */
    public boolean isEmailRegistered(String email){
        boolean isRegistered = false;
        Optional<AppUsers> appUsers = Optional.ofNullable(userRepo.findByEmail(email));
        if(!appUsers.isPresent() && !appUsers.get().isDisabled()){
            isRegistered = true;
        } else {
            isRegistered = false;
        }
        return isRegistered;
    }
    
    /**
     *
     * @param email
     * @return
     */
    public boolean updateLoginTries(final String email) {
       //TODO implement the method
        return false;
    }
    
    
    /*private List<GrantedAuthority> getUserAuthorities(String email) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Roles role : roleSet) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return grantedAuthorities;
    }
    
    public Collection<String> getLoggedInUsers() {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(Object::toString)
                .collect(Collectors.toList());
    }
    
     */

    /**
     *
     * @param roleId
     * @return AuthoritiesMaster
     */
    public AuthoritiesMaster getMasterAuthoritiesUsingRoleId(String roleId) {
        return authoritiesMasterRepo.findByRoleId(roleId);
    }


    public List<AuthoritiesMaster> getMasterAuthoritiesUsingRolesId(final List<String> roleIdList) {
        return  authoritiesMasterRepo.findByRolesId(roleIdList);
    }
}
