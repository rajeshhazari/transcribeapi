package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUsersRole {
	private String roleId;
	private String roleDesc;
	private Integer maxUploadFileSizeMb;
	private Integer maxFilesCount;

}
