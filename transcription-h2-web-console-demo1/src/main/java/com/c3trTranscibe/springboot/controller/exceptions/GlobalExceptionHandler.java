package com.c3trTranscibe.springboot.controller.exceptions;

import java.time.LocalDateTime;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.c3trTranscibe.springboot.repository.exceptions.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 	@ExceptionHandler(NoHandlerFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ApiError noHandlerFoundException(
	            NoHandlerFoundException ex) {

	        int code = HttpStatus.NOT_FOUND.value();
	        String message = "No handler found for your request.";
	        return new ApiError(code, message);
	    }
	 
	 @ExceptionHandler({ResourceNotFoundException.class,Exception.class})
     public ResponseEntity<ApiError> resourceNotFoundException(ResourceNotFoundException ex, HttpRequest request) {
         ApiError errorDetails = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(),LocalDateTime.now());
         return new ResponseEntity<ApiError>(errorDetails, HttpStatus.NOT_FOUND);
     }
	 
	 @ExceptionHandler({FileSizeLimitExceededException.class})
     public ResponseEntity<ApiError> fileploadMaxLimitException(FileSizeLimitExceededException ex, HttpRequest request) {
         ApiError errorDetails = new ApiError(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage(),LocalDateTime.now());
         return new ResponseEntity<ApiError>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
     }
	 
	 
	 @ExceptionHandler({ AccessDeniedException.class })
	    public ResponseEntity<Object> handleAccessDeniedException(
	      Exception ex, WebRequest request) {
	        return new ResponseEntity<Object>(
	          "Access denied. Please Register.", new HttpHeaders(), HttpStatus.FORBIDDEN);
	    }
	 
	 @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
	 public ResponseEntity<ApiError> handleUserNotFounDexception(Exception uex, HttpRequest req){
	 		final String message = "Email/Username Not found, please check your Email/Username";
		 ApiError errorDetails = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message + uex.getMessage(),LocalDateTime.now());
		 return new ResponseEntity<ApiError>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 
}
