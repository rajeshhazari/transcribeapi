/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author rajesh
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthUserProfileDto extends BaseResponseDto{

	private String username;
	private String email;
	private Boolean enabled;
	private Boolean locked;
	private Boolean verified;
	private Boolean active;
	public String firstName;
	public String lastName;
	public String address1;
	public String address2;
	public String city;
	public String state;
	public String country;
	public String zipcode;
	public String altEmail;
	private LocalDateTime registeredDate;
	private LocalDateTime lastLoggedin;
	
}
