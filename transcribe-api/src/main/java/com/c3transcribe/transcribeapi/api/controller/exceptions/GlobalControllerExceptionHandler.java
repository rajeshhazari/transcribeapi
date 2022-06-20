package com.c3transcribe.transcribeapi.api.controller.exceptions;

import com.c3transcribe.transcribeapi.api.models.AppError;
import com.c3transcribe.transcribeapi.api.services.exceptions.EmailNotFoundException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.time.Instant;

import static org.apache.logging.log4j.LogManager.getLogger;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    
    private static final Logger logger = getLogger(GlobalControllerExceptionHandler.class);
    
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
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Object> handleJsonTokenSignature
            (
                    SignatureException ex,
                    HttpHeaders headers, HttpStatus status, WebRequest request)     {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(),
                "Invalid token: "+ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<AppError> handleAllRuntimeApiExceptions(RuntimeException ex) {
        AppError errorDetails = new AppError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public final ResponseEntity<AppError> handleAuthenticationCredentialsNotFoundApiExceptions(AuthenticationCredentialsNotFoundException ex) {
        AppError errorDetails = new AppError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<AppError> handleBadCredentialsNotFoundApiExceptions(BadCredentialsException ex, HttpServletRequest request) {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MalformedURLException.class)
    public final ResponseEntity<AppError> handleMalformedURLException(MalformedURLException ex, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String fileName = request.getParameter("name");
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException ex, HttpRequest request) {
        logger.warn(ex.getMessage());
        AppError errorDetails = new AppError(HttpStatus.PAYLOAD_TOO_LARGE.value(), ex.getMessage(), Instant.now());
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultiPartException(MultipartException ex, HttpRequest request) {
        logger.warn(ex.getMessage());
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), Instant.now());
        return new ResponseEntity<AppError>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<?> emailNotFoundException(EmailNotFoundException ex, HttpRequest request) {
        AppError errorDetails = new AppError(HttpStatus.BAD_REQUEST.value(), "Entered Email is not found or not valid:"+ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    
}
