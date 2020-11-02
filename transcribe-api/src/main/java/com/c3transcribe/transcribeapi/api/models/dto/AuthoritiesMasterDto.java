package com.c3transcribe.transcribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data @AllArgsConstructor @NoArgsConstructor
public class AuthoritiesMasterDto {
    
    private String roleId;
    private String roleDesc;
    private Integer maxUploadFileSize;
    private Integer maxFilesCount;

}
