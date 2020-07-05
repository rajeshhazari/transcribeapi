package com.rajesh.transcribe.transribeapi.api.controller.profile;

import com.rajesh.transcribe.transribeapi.api.models.UserProfileChangeRequestInput;
import com.rajesh.transcribe.transribeapi.api.services.AppEmailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    
    private final Logger logger = LoggerFactory.getLogger(ProfileSettingController.class);
    private final ServletConfig servletConfig;
    private AppEmailServiceImpl appEmailServiceImpl;
    @Autowired
    public ProfileSettingController(ServletConfig config, AppEmailServiceImpl appEmailServiceImpl) {
        this.servletConfig = config;
        this.appEmailServiceImpl = appEmailServiceImpl;
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
    @RequestMapping("/resetpwd")
    public @ResponseBody    ResponseEntity <Map<String,String>> resetPassword(@RequestBody UserProfileChangeRequestInput authUserProfileDto, HttpServletRequest request, HttpServletResponse response){
        Map<String, String> responseMap = new ConcurrentHashMap<>();
        //TODO implement logic to check for email and check if user exists and send email
        return ResponseEntity.ok(responseMap);
    }
}
