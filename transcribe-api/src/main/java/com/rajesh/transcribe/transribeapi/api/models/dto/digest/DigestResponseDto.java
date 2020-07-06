package com.rajesh.transcribe.transribeapi.api.models.dto.digest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rajesh.transcribe.transribeapi.api.models.dto.BaseResponseDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigestResponseDto extends BaseResponseDto {
    private String data;
    private String algorithm;
}
