/**
 * 
 */
package com.c3transcribe.transcribeapi.api.models.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rajesh
 *
 */
public record AuthUserProfileDto (
		String username, String email, boolean enabled, boolean locked, boolean verified, boolean active,
		String firstName, String lastName, String address1, String address2, String city, String state,
		String country, String zipcode, String altEmail, LocalDateTime registeredDate,
		LocalDateTime lastLogged, List<AuthUsersRole> authUsersRolesList, String systemSupportedFileTypes,
		String systemMaxUploadFileSizeMb, Integer availableFilesCount){
	
}

