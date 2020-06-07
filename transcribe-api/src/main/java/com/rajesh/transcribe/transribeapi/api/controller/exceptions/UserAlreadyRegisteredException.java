package com.rajesh.transcribe.transribeapi.api.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyRegisteredException extends RuntimeException{
    
    public UserAlreadyRegisteredException(String e) {
        super(e);
    }
}
