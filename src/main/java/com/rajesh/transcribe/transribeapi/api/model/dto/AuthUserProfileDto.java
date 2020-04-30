/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.model.dto;

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
public class AuthUserProfileDto {

	private String username;
	private String email;
	private String jwtToken;
	private Boolean enabled;
	private Boolean locked;
	private Boolean verified;
	private Boolean active;
	private LocalDateTime registeredDate;
	private LocalDateTime lastLogedin;
	
}
