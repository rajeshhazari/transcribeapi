package com.rajesh.transcribe.transribeapi.api.controller.exceptions;

import com.rajesh.transcribe.transribeapi.api.models.AppError;
import com.rajesh.transcribe.transribeapi.api.repository.exceptions.UserNotFoundException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    
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
    
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<?> userAlreadyRegisteredException(UserAlreadyRegisteredException ex, HttpRequest request) {
        AppError errorDetails = new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> emailExceptions(MessagingException ex, HttpRequest request) {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (
                    MethodArgumentNotValidException ex,
                    HttpHeaders headers, HttpStatus status, WebRequest request)     {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), Instant.now());
    
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Object> handleJsonTokenSignature
            (
                    SignatureException ex,
                    HttpHeaders headers, HttpStatus status, WebRequest request)     {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), Instant.now());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
