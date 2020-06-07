package com.rajesh.transcribe.transribeapi.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rajesh.transcribe.transribeapi.api.models.dto.BaseResponseDto;
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
