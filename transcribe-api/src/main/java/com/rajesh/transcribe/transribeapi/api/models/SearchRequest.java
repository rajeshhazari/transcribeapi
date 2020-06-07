package com.rajesh.transcribe.transribeapi.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String token =  null;
    private String query = null;
    private String email = null;
    private String transcribtionReqId = null;
    private String fileName = null;
    
}
