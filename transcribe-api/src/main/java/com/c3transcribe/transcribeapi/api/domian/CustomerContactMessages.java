package com.c3transcribe.transcribeapi.api.domian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data @AllArgsConstructor @NoArgsConstructor
@Table("CUSTOMER_CONTACT_MESSAGES")
public class  CustomerContactMessages{
    
    @Id
    private Long id;
    
    private String message;
    private String email;
    private String firstName;
    private String lastName;
    
}
