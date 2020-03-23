package com.c3trTranscibe.springboot.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("APPUSERS")
@AllArgsConstructor
@NoArgsConstructor
@Data
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
