package com.c3transcribe.transcribeapi.api.domian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/*public class AppUsers (
	
	@Id Long userid,  String username, @Column(value = "first_name") String firstName, @Column(value = "last_name")
	 String lastName,   String email,	@Column(value = "phone_number") String phoneNumber, boolean active, boolean disabled, boolean verified,
	 boolean locked, String zipcode, String password , @Column(value = "registered_date")
	 LocalDateTime registeredDate, 	@Column(value = "registered_date")
	 LocalDateTime lastModified,
	@MappedCollection(idColumn="userid", keyColumn="userid") List<AppUsersAuth> appUsersAuthList ){
	
	public AppUsers() {
		this(null, null,null ,null ,null ,null ,false ,false ,
				false,false ,null ,null ,null ,null ,null );
	}
}*/

@Builder
@Table("APPUSERS") @AllArgsConstructor
@NoArgsConstructor  @Data
public class AppUsers {
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
	
	
}
