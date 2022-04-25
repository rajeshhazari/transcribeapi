package com.c3transcribe.transcribeapi.api.models.dto;

import com.c3transcribe.transcribeapi.api.models.AppError;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown=true)
public record   BaseResponseDto (AppError error) {
}
