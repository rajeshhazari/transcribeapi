package com.rajesh.transcribe.transribeapi.api.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Table("APPUSERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUsers implements Serializable {
	@Id
	private  Long userid;
	private String username;
	@Column(value = "first_name")
	private String firstName;
	@Column(value = "last_name")
	private String lastName;
	private String email;
	@Column(value = "phone_number")
	private String phoneNumber;
	private boolean active;
	private boolean disabled;
	private boolean verified;
	private boolean locked;
	private String zipcode;
	private String password;
	@Column(value = "registered_date")
	private LocalDateTime registeredDate;
	@Column(value = "registered_date")
	private LocalDateTime lastModified;
	
	private List<AppUsersAuth> appUsersAuthList;
	
	
	
}

@Table("appusers_auth")
@NoArgsConstructor
@Data
class AppUsersAuth {
	@Column("auth_user_id")
	private String authUserId;
	private String userid;
	private String email;
	@Column("role_id")
	private String roleId;
	@Column("username")
	private String username;
	@Column("updated_time")
	private Date updatedTime;
	
	private AppUsersAuthRolesMaster appUsersAuthRolesMaster;
}

@Table("appusers_auth")
@NoArgsConstructor @AllArgsConstructor
@Data
class AppUsersAuthRolesMaster{
	@Column("role_id")
	private String roleId;
	@Column("roledesc")
	private String roledesc;
	@Column("max_file_size")
	private Integer maxUploadFileSize;
	@Column("max_number_files")
	private String maxFilesCount;
	
	
}
