package com.c3transcribe.transcribeapi.api.models;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record AppError ( int code,  HttpStatus httpStatus, String message,  Instant timestamp ) {
   
    
    public AppError(final HttpStatus status, final String message) {
        this(status.value(),  status,  message, Instant.now());
    }
    
    public AppError(final int value, final String message, final Instant now) {
        
        this(value, HttpStatus.resolve(value), message,  now);
        
    }
}
