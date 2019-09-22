/**
 *
 */
package com.c3trTranscibe.springboot.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder ({"ttoken","trancriptionId", "isFormatted", "text"})
public final class TranscriptionResponse {

	@NotEmpty
	private String transcribeText = null;
	private Long  trancriptionId = 0L;
	private String ttoken = null;
	private boolean isFormatted = false;
	private List<String> wordsList = null;

	private void TranscriptionResponse() {}

	public TranscriptionResponse(String text, Long trancriptionId, String ttoken, boolean isFormatted, List<String> wordsList) {
		super();
		this.transcribeText = text;
		this.trancriptionId = trancriptionId;
		this.ttoken = ttoken;
		this.isFormatted = isFormatted;
		this.wordsList = wordsList;
	}

	/**
	 * @return the ttoken
	 */
	public String getTtoken() {
		return ttoken;
	}

	/**
	 * @return the isFormatted
	 */
	public boolean isFormatted() {
		return isFormatted;
	}

	/**
	 * @return the text
	 */
	public String getTranscribeText() {
		return transcribeText;
	}

	/**
	 * @return the trancriptionId
	 */
	public Long getTrancriptionId() {
		return trancriptionId;
	}

}
