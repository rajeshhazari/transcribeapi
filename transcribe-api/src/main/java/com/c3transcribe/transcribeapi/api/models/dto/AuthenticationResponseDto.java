package com.c3transcribe.transcribeapi.api.models.dto;

import com.c3transcribe.transcribeapi.api.models.AppError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponseDto {
        //extends BaseResponseDto {
    
    private String jwt;
    private String email;
    private LocalDateTime lastLoggedIn;
    private AppError appError;
}
