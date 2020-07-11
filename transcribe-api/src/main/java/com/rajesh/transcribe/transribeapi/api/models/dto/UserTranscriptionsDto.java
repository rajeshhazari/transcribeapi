package com.rajesh.transcribe.transribeapi.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTranscriptionsDto {
    private Integer requestId;
    private String message;
}