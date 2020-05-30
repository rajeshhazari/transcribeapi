package com.rajesh.transcribe.transribeapi.api.services;

import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthUserProfileDto;
import com.rajesh.transcribe.transribeapi.api.repository.AppUsersRepository;
import com.rajesh.transcribe.transribeapi.api.repository.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    
    private Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    
    @Autowired
    Environment env;
    
    @Autowired
    private PasswordEncoder bcryptEncoder;
    
    @Autowired
    AppUsersRepository userRepo;
    
    @Autowired
    DataSource dataSource;
    
    /*@Autowired
    private SessionRegistry sessionRegistry;*/
    
    
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //List<AppUsers> appuser = userRepo.findByEmail(s);
        Optional<AppUsers> user = null;
        try{
            user = userRepo.findByEmail(s);
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + s);
        }
        logger.debug("user retrived:: %s  for user %s",user ,s );
    
        if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + s);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is locker. Please Contact us for more info: " + s);
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
        //List<AppUsers> appuser = userRepo.findByEmail(s);
        Optional<AppUsers> user = null;
        AuthUserProfileDto userDto = new AuthUserProfileDto();
        try{
            user = userRepo.findByEmail(email);
        }catch (BadCredentialsException bex){
            throw new BadCredentialsException("Bad credentials. Please check your username/password: " + email);
        }
        logger.debug("user retrived:: %s  for user %s",user ,email );
        if (!user.isEmpty() || user.isPresent()) {
            userDto.setUsername(user.get().getEmail());
            userDto.setActive(user.get().isActive());
            userDto.setLocked(user.get().isLocked());
            userDto.setEnabled(user.get().isDisabled());
            userDto.setLastLogedin(user.get().getLastModified());
        }
        if (user.isEmpty() || !user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }else if (user.isPresent() && user.get().getUsername().isEmpty()) {
            throw new UsernameNotFoundException("There is some problem with your account, please contact us to resolve your issue: " + email);
        }else if (user.isPresent() && user.get().isLocked()) {
            throw new UsernameNotFoundException("Your account is locker. Please Contact us for more info: " + email);
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
        };
        return user.get();
    }
    
    /**
     *
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    public AppUsers getUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUsers> user = userRepo.findByEmail(email);
        
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
        AppUsers newUser = new AppUsers();
        newUser.setUsername(regUser.getUsername());
        newUser.setPassword(bcryptEncoder.encode(regUser.getPassword()));
        newUser.setEmail(regUser.getEmail());
        newUser.setFirstName(regUser.getFirstName());
        newUser.setLastName(regUser.getLastName());
        newUser.setUsername(regUser.getUsername());
        newUser.setZipcode(regUser.getZipcode());
        
        logger.debug("New User added - %s",regUser.getUsername());
        return userRepo.save(newUser);
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
        return getUserByEmail(((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername());
    }
    
    
    /*private List<GrantedAuthority> getUserAuthorities(Set<Role> roleSet) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roleSet) {
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
}
