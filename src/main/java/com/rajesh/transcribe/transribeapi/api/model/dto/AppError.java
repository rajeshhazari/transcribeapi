package com.rajesh.transcribe.transribeapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    private int code;
    private String message;
    private Instant timestamp;
    
    public AppError(final HttpStatus status, final String message) {
        this.code = status.value();
        this.message = message;
        this.timestamp = Instant.now();
    }
}
