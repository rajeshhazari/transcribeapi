package com.c3transcribe.transcribeapi.api.models;

import com.c3transcribe.transcribeapi.api.models.dto.BaseResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class CaptchaResponse extends BaseResponseDto {

    private Boolean success;
    private Date timestamp;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorCodes;

}
