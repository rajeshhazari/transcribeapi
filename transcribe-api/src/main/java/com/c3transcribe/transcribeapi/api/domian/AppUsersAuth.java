package com.c3transcribe.transcribeapi.api.domian;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("APPUSERS_AUTH")
@Data @Builder
public 	class AppUsersAuth {
    @Column("auth_user_id")
    private Long authUserId;
    @Column("userid")
    private  Long userid;
    private String email;
    @Column("role_id")
    private String roleId;
    @Column("username")
    private String username;
    @Column("updated_time")
    private Date updatedTime;
    
    @Column("token")
    private String token;
    
    @Column("token_expiry")
    private String tokenExpiryDate;
    
    @Column("token_iat")
    private  String tokenIssuesAt;

}
