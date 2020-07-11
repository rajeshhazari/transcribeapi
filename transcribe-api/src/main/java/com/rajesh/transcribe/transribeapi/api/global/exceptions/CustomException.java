package com.rajesh.transcribe.transribeapi.api.global.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    
    public  String message;
    public HttpStatus httpStatus;
    public CustomException(final String message, final HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
}
