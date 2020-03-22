/**
 *
 */
package com.rajesh.transcribe.transribeapi.api.models.sphinx4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"ttoken","trancribtionId", "isFormatted", "text"})
public final class TranscribtionResponse {

	@NotEmpty
	private String transcribedText = null;
	private String  trancribtionId = null;
	private String ttoken = null;
	private boolean isFormatted = false;
	private List<String> wordsList = null;
	private List<String> wordConfidenceList = null;

	private void TranscriptionResponse() {}

	public TranscribtionResponse(String text, String trancribtionId, String ttoken, boolean isFormatted, List<String> wordsList) {
		super();
		this.transcribedText = text;
		this.trancribtionId = trancribtionId;
		this.ttoken = ttoken;
		this.isFormatted = isFormatted;
		this.wordsList = wordsList;
	}
	
	public TranscribtionResponse(String text, String trancribtionId, String ttoken, boolean isFormatted, List<String> wordsList, List<String> wordConfidenceList) {
		super();
		this.transcribedText = text;
		this.trancribtionId = trancribtionId;
		this.ttoken = ttoken;
		this.isFormatted = isFormatted;
		this.wordsList = wordsList;
		this.wordConfidenceList = wordConfidenceList;
	}

	

}
