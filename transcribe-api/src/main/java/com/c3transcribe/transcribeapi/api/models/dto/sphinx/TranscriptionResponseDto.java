/**
 *
 */
package com.c3transcribe.transcribeapi.api.models.dto.sphinx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonPropertyOrder({"token","transcrptionId", "isFormatted", "transcribedText","fileName"})
public final class TranscriptionResponseDto {
		//extends BaseResponseDto {

	@NotEmpty
	private String transcribedText ;
	@NotEmpty
	private String transcriptionId ;
	private String token ;
	private boolean isAutoTranscriptionFormatted = false;
	private List<String> termsList ;
	private List<String> wordConfidenceList ;
	private String fileName ;


	

}
