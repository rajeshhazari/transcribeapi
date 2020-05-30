package com.rajesh.transcribe.transribeapi.api.controller.exceptions;

import com.rajesh.transcribe.transribeapi.api.models.dto.AppError;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError noHandlerFoundException(
            NoHandlerFoundException ex) {
        
        String message = "No handler found for your request.";
    return new AppError(HttpStatus.NOT_FOUND, message);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, HttpRequest request) {
        AppError errorDetails = new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
}
