/**
 *
 */
package com.c3trTranscibe.springboot.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.c3trTranscibe.springboot.domain.Users;

import lombok.AllArgsConstructor;

/**
 * @author rajesh
 *
 */
@Table("registeredUsers")
@AllArgsConstructor
@lombok.Data
public class RegisteredUser {

	@Id
	public Integer ID;
	public String username;
	public String email;
	public String password;

	public RegisteredUser(String email, String password) {

		this.email = email;
		this.password = password;
	}

}
