/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rajesh
 *
 */
@Data @AllArgsConstructor
@NoArgsConstructor
public class AuthUserProfileDto extends BaseResponseDto{

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
	private String systemSupportedFileTypes;
	private String systemMaxUploadFileSizeMb;
	private Integer availableFilesCount;
	
}

