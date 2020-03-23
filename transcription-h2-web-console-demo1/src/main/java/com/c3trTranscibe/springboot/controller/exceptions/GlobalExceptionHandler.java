package com.c3trTranscibe.springboot.controller.exceptions;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.c3trTranscibe.springboot.exceptions.UserNotFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

	 	/*@ExceptionHandler(NoHandlerFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<?> noHandlerFoundException(
	            NoHandlerFoundException ex, HttpResponse res) {

	        String message = "No handler found for your request.";
	        ApiError appError =new ApiError();
	        appError.setCode(HttpStatus.NOT_FOUND.value());
	        appError.setMessage(message);
	        appError.setMessage("Not found");
	        
	        return new ResponseEntity<ApiError>(appError, HttpStatus.NOT_FOUND);
	    }*/
	 
	 @ExceptionHandler({ResourceNotFoundException.class,Exception.class})
     public ResponseEntity<ApiError> resourceNotFoundException(ResourceNotFoundException ex, HttpResponse res) {
		 ApiError appError =new ApiError();
		 appError.setCode(HttpStatus.NOT_FOUND.value());
		 appError.setMessage(ex.getMessage());
		 appError.setTimestamp(LocalDateTime.now());
         return new ResponseEntity<ApiError>(appError, HttpStatus.NOT_FOUND);
     }
	 
	 @ExceptionHandler({FileSizeLimitExceededException.class})
     public ResponseEntity<ApiError> fileploadMaxLimitException(FileSizeLimitExceededException ex, HttpResponse request) {
		 ApiError appError =new ApiError();
		 appError.setCode(HttpStatus.NOT_ACCEPTABLE.value());
		 appError.setMessage(ex.getMessage());
		 appError.setTimestamp(LocalDateTime.now());
		
		 return new ResponseEntity<ApiError>(appError, HttpStatus.NOT_ACCEPTABLE);
     }
	 
	 
	 @ExceptionHandler({ AccessDeniedException.class })
	    public ResponseEntity<Object> handleAccessDeniedException(
	      Exception ex, WebRequest request) {
	        return new ResponseEntity<Object>(
	          "Access denied. Please Register.", new HttpHeaders(), HttpStatus.FORBIDDEN);
	    }
	
	@ExceptionHandler({ BadCredentialsException.class })
	public ResponseEntity<Object> handleBadCredentialsException(
			Exception ex, WebRequest request) {
		ApiError appError =new ApiError();
		appError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		appError.setMessage(ex.getMessage());
		appError.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(appError,HttpStatus.UNAUTHORIZED);
	}
	 
	 @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class })
	 public ResponseEntity<ApiError> handleUserNotFounDexception(Exception uex, HttpResponse req){
	 		final String message = "Email/Username Not found, please check your Email/Username";
		 ApiError appError =new ApiError();
		 appError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		 appError.setMessage(message);
		 appError.setTimestamp(LocalDateTime.now());
		
		 return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	
	@ExceptionHandler({AuthenticationException.class})
	public ResponseEntity<ApiError> authenticationException(AuthenticationException ex, HttpResponse request) {
		ApiError appError =new ApiError();
		appError.setCode(HttpStatus.UNAUTHORIZED.value());
		appError.setMessage(ex.getMessage());
		appError.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ApiError>(appError, HttpStatus.UNAUTHORIZED);
	}
	 
}
