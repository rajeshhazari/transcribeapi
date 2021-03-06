/**
 *
 */
package com.c3transcribe.transcribeapi.api.models.dto.sphinx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.c3transcribe.transcribeapi.api.models.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"token","trancrptionId", "isFormatted", "text"})
public final class TranscriptionResponseDto extends BaseResponseDto {

	@NotEmpty
	private String transcribedText = null;
	private String  trancriptionId = null;
	private String token = null;
	private boolean isFormatted = false;
	private List<String> wordsList = null;
	private List<String> wordConfidenceList = null;
	private String fileName = null;


	

}
