package com.c3trTranscibe.springboot.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table(value = "users")
@AllArgsConstructor
@Getter
@Setter
public class Users {
	@Id
	private  Long userid;

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private boolean active;
	private boolean disabled;
	private boolean verified;
	private boolean locked;
	private String zipcode;
	private String password;
	
	
	public Users() {}

	


}
