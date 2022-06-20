package com.c3transcribe.transcribeapi.api.domian;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("USERREGVERIFYLOGDETAILS")
@Data @AllArgsConstructor @NoArgsConstructor
public class RegUserVerifyLogDetails {
    
    @Id
    @Column("id")
    public Integer id;
    @NonNull
    public String username;
    @NonNull
    @Column("email")
    public String email;
    private Integer code;
    public String confEmailToken;
    public String confEmailUrl;
    public boolean disabled;
    public boolean verified;
    public boolean verificationEmailSent;
    public Instant emailSentDate;
    public Instant verificationDate;
    public String verifiedRegClientIp;
    
}

