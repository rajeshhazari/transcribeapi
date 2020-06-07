package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDto implements Serializable {
    private String username;
    private String password;
    
}
