package com.c3trTranscibe.springboot.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(NoHandlerFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ApiError noHandlerFoundException(
	            NoHandlerFoundException ex) {

	        int code = 1000;
	        String message = "No handler found for your request.";
	        return new ApiError(code, message);
	    }
	 
	 @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, HttpRequest request) {
         ApiError errorDetails = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(),Instant.now());
         return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
     }
	 
}
