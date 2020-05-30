package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponseDto implements Serializable {

    private String jwt;
    private String email;
    private Date lastLoggedIn;
    private AppError error;
}
