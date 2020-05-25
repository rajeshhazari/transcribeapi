package com.rajesh.transcribe.transribeapi.api.models;

import com.rajesh.transcribe.transribeapi.api.model.dto.AppError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthenticationResponse implements Serializable {

    private String jwt;
    private String email;
    private Date lastLoggedIn;
    private AppError error;

}
