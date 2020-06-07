package com.rajesh.transcribe.transribeapi.api.domian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("CUSTOMERCONTACTMESSAGES")
public class  CustomerContactMessages{
    
    @Id
    private Long id;
    
    private String message;
    private String email;
    private String firstName;
    private String lastName;
    
}
