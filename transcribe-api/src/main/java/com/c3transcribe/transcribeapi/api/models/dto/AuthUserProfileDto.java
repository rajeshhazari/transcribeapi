/**
 * 
 */
package com.c3transcribe.transcribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rajesh
 *
 */
@Data @AllArgsConstructor @NoArgsConstructor
//public class AuthUserProfileDto extends BaseResponseDto{
// Lombok does not infer default constructor from record class
// This class is used to send to ui layer after successfull auth to UI or any consumer
public class AuthUserProfileDto {

	private String username;
	private String email;
	private boolean enabled;
	private boolean locked;
	private boolean verified;
	private boolean active;
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
	private List<AuthUsersRole> authUsersRolesList;
	private List<String> systemSupportedFileTypes;
	private String systemMaxUploadFileSizeMb;
	private Integer availableFilesCount;
	
}

