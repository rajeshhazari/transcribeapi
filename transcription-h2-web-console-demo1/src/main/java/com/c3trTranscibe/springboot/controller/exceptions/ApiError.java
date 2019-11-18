package com.c3trTranscibe.springboot.controller.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;


public class ApiError {

    private int code;
    private String message;
    private LocalDateTime timestamp;
    private String debugMessage;

    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(int code, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.code = status.value();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.code = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
	public ApiError() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

 
}