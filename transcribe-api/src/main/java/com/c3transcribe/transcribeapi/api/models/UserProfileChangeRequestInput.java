/**
 * 
 */
package com.c3transcribe.transcribeapi.api.models;

import com.c3transcribe.transcribeapi.api.models.dto.BaseResponseDto;
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
public class UserProfileChangeRequestInput extends BaseResponseDto {

	private String username;
	private String email;
	private String newEmail;
	public String firstName;
	public String newFirstName;
	public String lastName;
	public String newLastName;
	public String address1;
	public String newAddress1;
	public String address2;
	public String newAddress2;
	public String city;
	public String newCity;
	public String state;
	public String newState;
	public String country;
	public String newCountry;
	public String zipcode;
	public String newZipcode;
	public String altEmail;
	public String newPassword;
	
}
