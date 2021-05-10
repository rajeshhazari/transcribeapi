package com.c3transcribe.transcribeapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.solr.common.SolrInputDocument;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranscribedRespSolrInputDoc extends SolrInputDocument {
    private String transcribedRespText;
    private String transcribtionRequestId;
    private String email;
    private Date transcribedDate;
}
