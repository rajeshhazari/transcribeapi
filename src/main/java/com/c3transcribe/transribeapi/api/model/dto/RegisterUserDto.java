/**
 *
 */
package com.c3transcribe.transribeapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rajesh
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RegisterUserDto {

	public String username;
	public String email;
	public String fName;
	public String lName;
	public String password;
	public String altEmail;
	public String phoneNumber;
	public String address1;
	public String address2;
	public String city;
	public String state;
	
}
