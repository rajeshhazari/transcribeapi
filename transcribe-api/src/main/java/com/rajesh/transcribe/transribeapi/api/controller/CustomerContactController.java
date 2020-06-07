package com.rajesh.transcribe.transribeapi.api.controller;

import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationResponseDto;
import com.rajesh.transcribe.transribeapi.api.models.dto.CustomerContactMessagesDto;
import com.rajesh.transcribe.transribeapi.api.services.pub.CustomerContactMessagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(
        value = "CustomerContactController",
        description = "Controller for customer contact messages. ")
@RestController
@RequestMapping("/public/contactus")
public class CustomerContactController {
    
    private final Logger logger = LoggerFactory.getLogger(CustomerContactController.class);
    
    private CustomerContactMessagesService customerContactMessagesService;
    public CustomerContactController(CustomerContactMessagesService customerContactMessagesService) {
        this.customerContactMessagesService = customerContactMessagesService;
    }
    
    
    @ApiOperation(value = "Save customer contact messages", response = Boolean.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Saved."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/savemsg", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Boolean> saveMessage(CustomerContactMessagesDto customerContactMessagesDto, HttpServletRequest request, HttpServletResponse response){
        //TODO send an email to c3Transcribeops@email.com, there is nw contact message received.
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
    }
}
