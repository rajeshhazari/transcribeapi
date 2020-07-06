package com.rajesh.transcribe.transribeapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data @NoArgsConstructor  @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerContactMessagesDto extends BaseResponseDto{
    private int code;
    private String message;
    private String email;
    private String firstName;
    private String lastName;
    
}
