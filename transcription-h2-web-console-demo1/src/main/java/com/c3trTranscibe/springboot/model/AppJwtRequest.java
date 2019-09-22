/**
 * 
 */
package com.c3trTranscibe.springboot.model;

import java.io.Serializable;

/**
 * @author rajesh
 *
 */

public class AppJwtRequest implements Serializable {

	private String username;
	private String password;
	//need default constructor for JSON Parsing
	public AppJwtRequest()
	{
	}
	public AppJwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
