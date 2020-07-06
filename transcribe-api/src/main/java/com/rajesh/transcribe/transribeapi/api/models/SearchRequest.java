package com.rajesh.transcribe.transribeapi.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchRequest {
    private String query = null;
    private String email = null;
    private String TranscriptionReqId = null;
    private String fileName = null;
    private int page = 1;
    private int pageSize = 10;
    
}
