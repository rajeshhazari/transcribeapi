package com.c3transcribe.transcribeapi.api.controller.profile;

import com.c3transcribe.transcribeapi.api.domian.AppUsers;
import com.c3transcribe.transcribeapi.api.models.dto.AuthUserProfileDto;
import com.c3transcribe.transcribeapi.api.services.JwtUserDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("public/user")
public class UserProfileController {
    
    private JwtUserDetailsService jwtUserDetailsService;
    private PasswordEncoder bcryptEncoder;
    
    public UserProfileController (JwtUserDetailsService jwtUserDetailsService, PasswordEncoder bcryptEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.bcryptEncoder = bcryptEncoder;
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
    @PostMapping(value = "/{email}", produces = "application/json")
    public ResponseEntity<Boolean> updateProfile(
            @PathParam("email") @Validated @Email String email,
            HttpServletRequest request, HttpServletResponse response,
            @RequestAttribute AuthUserProfileDto authUserProfileDto
    ) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authorizedUser = (User) authentication.getPrincipal();
        final AppUsers appUsers = new AppUsers();
        BeanUtils.copyProperties(authUserProfileDto, appUsers);
        Boolean updateStatus = jwtUserDetailsService.save(appUsers) != null ? Boolean.TRUE : Boolean.FALSE;
        HttpStatus status = authUserProfileDto != null ?
                HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Boolean>(updateStatus, status);
    }
}
