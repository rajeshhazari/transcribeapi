package com.c3transcribe.transcribeapi.api.controller.profile;

import com.c3transcribe.transcribeapi.api.models.UserProfileChangeRequestInput;
import com.c3transcribe.transcribeapi.api.repository.AppUsersRepository;
import com.c3transcribe.transcribeapi.api.repository.AuthoritiesMasterRepo;
import com.c3transcribe.transcribeapi.api.services.AppEmailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api(
        value = "ProfileSettingController",
        description = "Default Profile setting controller. ")
@RestController
@RequestMapping("public/profile/settings")
public class ProfileSettingController {
    
    @Autowired
    Environment env;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String appMaxUplaodLimit;
    
    private final Logger logger = LoggerFactory.getLogger(ProfileSettingController.class);
    private final ServletConfig servletConfig;
    private AppEmailServiceImpl appEmailServiceImpl;
    private AppUsersRepository appUserRepo;
    private AuthoritiesMasterRepo authoritiesMasterRepo;
    private ModelMapper modelMapper;

    @Autowired
    public ProfileSettingController(ServletConfig config, AppEmailServiceImpl appEmailServiceImpl,
                                    AppUsersRepository appUserRepo, AuthoritiesMasterRepo authoritiesMasterRepo,
                                    ModelMapper modelMapper) {
        this.servletConfig = config;
        this.appEmailServiceImpl = appEmailServiceImpl;
        this.appUserRepo = appUserRepo;
        this.authoritiesMasterRepo = authoritiesMasterRepo;
        this.modelMapper = modelMapper;
    }
    
    @ApiOperation(value = "Request for change in email.", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Encrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping("/changeEmail")
    public @ResponseBody    ResponseEntity <Map<String,String>> changeEmail(@RequestBody UserProfileChangeRequestInput authUserProfileDto, HttpServletRequest request, HttpServletResponse response){
        Map<String, String> responseMap = new ConcurrentHashMap<>();
        return ResponseEntity.ok(responseMap);
    }
    
    
    @ApiOperation(value = "Request for reset password .", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Send reset password Email."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(name = "/resetpwd" , method=RequestMethod.POST)
    public @ResponseBody    ResponseEntity <Map<String,String>> resetPassword(@RequestBody UserProfileChangeRequestInput authUserProfileDto, HttpServletRequest request, HttpServletResponse response){
        Map<String, String> responseMap = new ConcurrentHashMap<>();
        //TODO implement logic to check for email and check if user exists and send email
        return ResponseEntity.ok(responseMap);
    }

    /**
     *
     * @param code
     * @param emailId (email id in base64 encode form)
     * @param req
     * @param session
     * @return
     *//*
    @GetMapping(value = "/public/confirmEmail/{email}/{code}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<? extends Object> confirmEmail(
            @PathVariable("code") @NotNull @NotBlank final String code,
            @PathVariable("email") @NotNull @NotBlank final String emailId,
            HttpServletRequestWrapper req,
            HttpSession session, @RequestHeader final Map<String, String> headers) {
        String encryptedEmail = req.getParameter("emailId");
        String decodedEmail =
                new String(Base64.getUrlDecoder().decode(encryptedEmail), StandardCharsets.UTF_8);
        // TODO move this logic to service method and handle other use cases
        Map<String, String> resp = new HashMap<>();
        final AtomicBoolean status = new AtomicBoolean(false);
        ResponseEntity<Map<String, String>> responseEntity = null;
        AppUsers user = jwtUserDetailsService.getUserByEmailAndInactive(decodedEmail);
        if(Objects.nonNull(user)) {
            if (user.getEmail().equalsIgnoreCase(decodedEmail)) {
                List<RegisteredUserVerifyLogDetials> registeredUserVerifyLogDetialsList = registeredUserRepo.findByEmailAndCode(user.getEmail(),Integer.parseInt(code));

                if(Objects.nonNull(registeredUserVerifyLogDetialsList) && registeredUserVerifyLogDetialsList.isEmpty() ){

                    resp.put("email", emailId);
                    resp.put("message", "Invalid code or email, Please register");
                    return  new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
                }else {
                    registeredUserVerifyLogDetialsList.stream().forEach(regUser ->
                    {
                        regUser.setVerified(true);
                        regUser.setVerifiedRegClientIp("RemoteAddr :: " +req.getRemoteAddr() +"  RemoteHost:: "+ req.getRemoteHost());
                        user.setActive(true);
                        user.setVerified(true);
                        if(Objects.nonNull(registeredUserRepo.save(regUser))) {
                            status.set(jwtUserDetailsService.activateUser(user.getEmail(), user));
                        }
                        if(status.get()){

                            resp.put("token", session.getId());
                            resp.put("message", "Email successfully verified, User is activated, Happy transcribing!.");
                            resp.put("userName", user.getUsername());
                            resp.put("email", user.getEmail());
                        }else{
                            resp.put("token", session.getId());
                            resp.put("message", " User is not activated, Please provide valid link!.");
                            resp.put("userName", user.getUsername());
                            resp.put("email", user.getEmail());

                        }

                    });
                    if(status.get()){
                        responseEntity = new ResponseEntity<>(resp,HttpStatus.OK);
                    }else {
                        responseEntity = new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                resp.put("message", " Email entered does not match!.");
                responseEntity = new ResponseEntity<>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }else {
            //TODO log the request attempts
            resp.put("message", " User is not registered, Please register!.");
            responseEntity = new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }*/
}
