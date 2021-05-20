package com.c3transcribe.transcribeapi.api.services;

import com.c3transcribe.transcribeapi.api.controller.exceptions.UserAlreadyRegisteredException;
import com.c3transcribe.transcribeapi.api.domian.AppUsersAuth;
import com.c3transcribe.transcribeapi.api.domian.AuthoritiesMaster;
import com.c3transcribe.transcribeapi.api.models.dto.AuthUserProfileDto;
import com.c3transcribe.transcribeapi.api.models.dto.AuthUsersRole;
import com.c3transcribe.transcribeapi.api.models.dto.RegisterUserRequest;
import com.c3transcribe.transcribeapi.api.models.dto.UserRegReqResponseDto;
import com.c3transcribe.transcribeapi.api.repository.AppUsersRepository;
import com.c3transcribe.transcribeapi.api.repository.AuthoritiesMasterRepo;
import com.c3transcribe.transcribeapi.api.repository.RegisteredUsersRepo;
import com.c3transcribe.transcribeapi.api.repository.exceptions.UserNotFoundException;
import com.c3transcribe.transcribeapi.api.domian.AppUsers;
import com.c3transcribe.transcribeapi.api.domian.RegisteredUserVerifyLogDetials;
import com.c3transcribe.transcribeapi.api.util.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.server.ServerErrorException;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.util.*;

import static com.c3transcribe.transcribeapi.api.util.SystemConstants.*;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    
    private Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    
    Environment env;
    private PasswordEncoder bcryptEncoder;
    private AppUsersRepository userRepo;
    private RegisteredUsersRepo registeredUsersRepo;
    private AppMailService appEmailServiceImpl;
    private DataSourceHealthIndicator dataSourceHealthIndicator;
    private SecureRandom secureRandom;
    private AuthoritiesMasterRepo authoritiesMasterRepo;
    private AuthenticationManager authenticationManager;
    private MultipartProperties multipartProperties;
    
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize appMaxUplaodLimit;
    
    public JwtUserDetailsService(final Environment env, final PasswordEncoder bcryptEncoder,
                                 final AppUsersRepository userRepo, final RegisteredUsersRepo registeredUsersRepo, final AppMailService appEmailServiceImpl, final DataSourceHealthIndicator dataSourceHealthIndicator, final SecureRandom secureRandom,
                                 final AuthoritiesMasterRepo authoritiesMasterRepo, final AuthenticationManager authenticationManager, final MultipartProperties multipartProperties) {
        this.env = env;
        this.bcryptEncoder = bcryptEncoder;
        this.userRepo = userRepo;
        this.registeredUsersRepo = registeredUsersRepo;
        this.appEmailServiceImpl = appEmailServiceImpl;
        this.dataSourceHealthIndicator = dataSourceHealthIndicator;
        this.secureRandom = secureRandom;
        this.authoritiesMasterRepo = authoritiesMasterRepo;
        this.authenticationManager = authenticationManager;
        this.multipartProperties = multipartProperties;
    }
    
    

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
     * @param password
     * @return @code UserDetails
     * @throws UsernameNotFoundException
     */
    @Transactional(readOnly = false)
    public UserDetails loadUserByUsername(String email, String password) throws UsernameNotFoundException {
        /*Optional<AppUsers> user = null;
        try{
            user = Optional.ofNullable(userRepo.findByEmail(email));
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }
        logger.debug("user retrived:: {}  for user {}",user ,email );
    
        if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + email);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is locker. Please Contact us for more info: " + email);
        }*/
    
        Optional<AppUsers> user = Optional.ofNullable(userRepo.findByEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        } else  if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + email);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is locker. Please Contact us for more info: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),
                AuthorityUtils.NO_AUTHORITIES);
    
    
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
        /*return new User("admin", "admin321",
                new ArrayList<>());*/
    }
    
    /**
     *
     * @param username
     * @param password
     * @throws Exception
     */
    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED"+e.getMessage(), e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS"+e.getMessage(),e);
        }
    }
    
    
    /**
     *
     * @param email
     * @return
     */
    @Transactional
    public AuthUserProfileDto getUserDto(String email) throws UsernameNotFoundException {
        AppUsers user = null;
        AuthUserProfileDto userDto = new AuthUserProfileDto();
        try{
            user = getUserByEmail(email);
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }catch (UserNotFoundException ex){
            throw new UserNotFoundException("User not found with email: " + email);
        }
        logger.debug("user retrived:: {}  for user {}",user ,email );
        if (Objects.nonNull(user)) {
            BeanUtils.copyProperties(user,userDto);
            userDto.setLastLoggedin(user.getLastModified());
        }
        if (user.getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact support team to resolve issue with your account: " + email);
        }else if ( user.isLocked()) {
            throw new UsernameNotFoundException("Your account is locked. Please contact support team for more info: " + email);
        }
    
        List<AuthUsersRole> roles = new ArrayList<>();
        user.getAppUsersAuthList()
                .stream()
                .forEach(appUsersAuth -> {
                    AuthUsersRole authUsersRole = new AuthUsersRole();
                    BeanUtils.copyProperties(appUsersAuth, authUsersRole);
                    roles.add(authUsersRole);
                });
        multipartProperties.getMaxFileSize();
        userDto.setSystemSupportedFileTypes(String.valueOf(multipartProperties.getMaxFileSize().toMegabytes()));
        userDto.setAuthUsersRolesList(roles);
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
    
    /**
     *
     * @param email
     * @return
     */
    public boolean saveLogedinJwtToken(final String email, final String jwtToken) {
        //TODO implement the method
        return false;
    }
    
    /**
     *
     * @param email
     * @return
     */
    public boolean deleteLoggedInToken(final String email, final String jwtToken) {
        //TODO implement the method
        return false;
    }
    
    
    /**
     *
     * @param email
     * @return
     */
    public boolean blackListLoggedInToken(final String email, final String jwtToken) {
        //TODO implement the method to save
        return false;
    }
    
    /**
     *
     * @param email
     * @return Boolean
     */
    public Boolean findIfEmailIsRegistered(final String email){
        final List<AppUsers> appUsersList = userRepo.findAllEmailList(email);
        return  appUsersList.isEmpty() ? false : true;
        /*
        if(appUsersList.isEmpty()){
            return false;
        }else {
            return true;
        }
        */
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
    
    /**
     *
     * @param email
     * @return @UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Optional<AppUsers> user = null;
        try{
            user = Optional.ofNullable(userRepo.findByEmail(email));
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.get().getEmail(),user.get().getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
        
    }
    
    /*@Transactional(readOnly = true)
    public boolean verifyRefreshToken(String email, String refreshToken){
        final AppUsersAuth aeppUsers = userRepo.findByEmail(email);
        
        if(appUsers.)
    }*/
}
