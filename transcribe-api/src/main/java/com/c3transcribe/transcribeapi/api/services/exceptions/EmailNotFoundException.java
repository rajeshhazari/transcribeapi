package com.c3transcribe.transcribeapi.api.services.exceptions;

public class EmailNotFoundException extends Exception {
    
    public EmailNotFoundException(String msg) {
        super(msg);
    }
    
    public EmailNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
    
}
