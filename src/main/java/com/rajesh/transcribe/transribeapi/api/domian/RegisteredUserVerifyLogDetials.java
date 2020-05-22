package com.rajesh.transcribe.transribeapi.api.domian;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("USERREGVERIFYLOGDETIALS")
@Data
public class RegisteredUserVerifyLogDetials {
    
    @Id
    @Column("id")
    public Integer ID;
    public String username;
    public String email;
    public String confEmailToken;
    public String confEmailUrl;
    public boolean disabled;
    public boolean verified;
    public boolean verificationEmailSent;
    public Date emailSentDate;
    public Date verificationDate;
    public String verifiedRegClientIp;
    
    
}
