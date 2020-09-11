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
	private List<AuthUsersRole> authUsersRolesList;
	private Integer systemSupportedFileTypes;
	private Integer systemMaxUploadFileSizeMb;
	private Integer availableFilesCount;
	
}

@Data
class AuthUsersRole {
	private String roleId;
	private String roleDesc;
	private Integer maxUploadFileSizeMb;
	private Integer maxFilesCount;

}
