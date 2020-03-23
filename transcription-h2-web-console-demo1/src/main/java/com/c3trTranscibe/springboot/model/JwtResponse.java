/**
 *
 */
package com.c3trTranscibe.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rajesh
 *
 */
@Getter @Setter
@AllArgsConstructor
public class JwtResponse  implements Serializable {

	private final String jwttoken;
	private final String userName;

	public String getToken() {
	return this.jwttoken;
	}
	}
