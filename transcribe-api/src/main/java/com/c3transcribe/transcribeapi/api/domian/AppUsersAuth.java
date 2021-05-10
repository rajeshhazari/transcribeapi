package com.c3transcribe.transcribeapi.api.domian;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("appusers_auth")
@Data
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

}
