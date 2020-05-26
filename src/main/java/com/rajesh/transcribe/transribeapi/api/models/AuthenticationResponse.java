package com.rajesh.transcribe.transribeapi.api.models;

import com.rajesh.transcribe.transribeapi.api.model.dto.AppError;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponse implements Serializable {

    private String jwt;
    private String email;
    private Date lastLoggedIn;
    private AppError error;
}
