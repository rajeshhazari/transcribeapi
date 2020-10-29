package com.rajesh.transcribe.transribeapi.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AppError {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private Instant timestamp;
    
    public AppError(final HttpStatus status, final String message) {
        this.code = status.value();
        this.httpStatus = status;
        this.message = message;
        this.timestamp = Instant.now();
    }
    
    public AppError(final int value, final String message, final Instant now) {
        this.code = value;
        this.message = message;
        this.timestamp = now;
    }
}
