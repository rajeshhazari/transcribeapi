/**
 *
 */
package com.c3trTranscibe.springboot.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder ({"ttoken","trancriptionId", "isFormatted", "text"})
public final class TranscriptionResponse {

	@NotEmpty
	private String transcribeText = null;
	private String  trancriptionId = null;
	private String ttoken = null;
	private boolean isFormatted = false;
	private List<String> wordsList = null;
	private List<String> wordConfidenceList = null;

	private void TranscriptionResponse() {}

	public TranscriptionResponse(String text, String trancriptionId, String ttoken, boolean isFormatted, List<String> wordsList) {
		super();
		this.transcribeText = text;
		this.trancriptionId = trancriptionId;
		this.ttoken = ttoken;
		this.isFormatted = isFormatted;
		this.wordsList = wordsList;
	}
	
	public TranscriptionResponse(String text, String trancriptionId, String ttoken, boolean isFormatted, List<String> wordsList, List<String> wordConfidenceList) {
		super();
		this.transcribeText = text;
		this.trancriptionId = trancriptionId;
		this.ttoken = ttoken;
		this.isFormatted = isFormatted;
		this.wordsList = wordsList;
		this.wordConfidenceList = wordConfidenceList;
	}

	

}
