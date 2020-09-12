package com.rajesh.transcribe.transribeapi.api.controller;

import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AppInfoController {
    
    @Autowired
    private BuildProperties buildProperties;
    
    
    @ApiOperation(value = "Give Api app build and release info. ", response = ResponseEntity.class, httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Application info received."),
                    @ApiResponse(code = 401, message = "You are not authorized to login or don't have correct role."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN','ROLE_DEVOPS'})")
    @RequestMapping(value= "/version" , method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<BuildProperties> greeting(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(buildProperties);
    }
    
}
