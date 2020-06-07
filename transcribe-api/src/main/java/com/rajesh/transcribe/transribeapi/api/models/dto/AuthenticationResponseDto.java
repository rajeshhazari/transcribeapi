package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto extends BaseResponseDto {

    private String jwt;
    private String email;
    private Date lastLoggedIn;
}
