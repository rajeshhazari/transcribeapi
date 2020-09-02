package com.rajesh.transcribe.transribeapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegReqResponseDto {
    private int code;
    private String message;
    private String errorMessage;
    private String email;
    private boolean isAlreadyRegistered;
    private boolean isDisabledEmail;
    private boolean verificationPending;
    
}
