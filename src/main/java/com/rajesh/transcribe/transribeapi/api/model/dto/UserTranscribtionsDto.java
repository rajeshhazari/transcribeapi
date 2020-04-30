package com.rajesh.transcribe.transribeapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTranscribtionsDto {
    private Integer requestId;
    private String message;
}
