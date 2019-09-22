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
	public JwtResponse(String jwttoken) {
	this.jwttoken = jwttoken;
	}
	public String getToken() {
	return this.jwttoken;
	}
	}
