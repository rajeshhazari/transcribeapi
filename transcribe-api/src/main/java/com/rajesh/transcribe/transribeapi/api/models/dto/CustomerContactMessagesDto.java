package com.rajesh.transcribe.transribeapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerContactMessagesDto {
    private int code;
    private String message;
    private String email;
    private String firstName;
    private String lastName;
    
}
