package com.rajesh.transcribe.transribeapi.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthenticationResponse implements Serializable {

    private String jwt;
    private String email;

}
