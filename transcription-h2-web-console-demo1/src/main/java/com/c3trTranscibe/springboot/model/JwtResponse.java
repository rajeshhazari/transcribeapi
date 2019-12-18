/**
 *
 */
package com.c3trTranscibe.springboot.model;

import java.io.Serializable;

/**
 * @author rajesh
 *
 */
public class JwtResponse  implements Serializable {

	private final String jwttoken;
	private final String userName;
	public JwtResponse(String jwttoken, String userName) {
	this.jwttoken = jwttoken;
		this.userName = userName;
	}
	public String getToken() {
	return this.jwttoken;
	}
	}
