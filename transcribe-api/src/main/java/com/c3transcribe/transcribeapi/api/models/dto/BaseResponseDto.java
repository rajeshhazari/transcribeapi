package com.c3transcribe.transcribeapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.c3transcribe.transcribeapi.api.models.AppError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract  class BaseResponseDto implements Serializable {
    private AppError error;
}
