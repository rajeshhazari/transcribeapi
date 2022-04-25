/**
 *
 */
package com.c3transcribe.transcribeapi.api.models.dto;

import lombok.*;

import java.util.Map;

/**
 * @author rajesh
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterUserRequest {

	public String username;
	public String email;
	public String firstName;
	public String lastName;
	public String address1;
	public String address2;
	public String city;
	public String state;
	public String country;
	public String zipcode;
	public String altEmail;
	public String password;
	public Map<String,String> clientInfoDetailsMap;
	
}
