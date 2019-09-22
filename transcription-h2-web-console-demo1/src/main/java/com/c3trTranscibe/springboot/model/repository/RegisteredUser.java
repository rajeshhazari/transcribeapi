/**
 *
 */
package com.c3trTranscibe.springboot.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rajesh
 *
 */
@Table("registeredUsers")
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

}
