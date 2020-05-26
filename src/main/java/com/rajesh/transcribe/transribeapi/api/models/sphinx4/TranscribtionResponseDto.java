/**
 *
 */
package com.rajesh.transcribe.transribeapi.api.models.sphinx4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rajesh.transcribe.transribeapi.api.model.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"token","trancribtionId", "isFormatted", "text"})
public final class TranscribtionResponseDto extends BaseResponseDto {

	@NotEmpty
	private String transcribedText = null;
	private String  trancribtionId = null;
	private String token = null;
	private boolean isFormatted = false;
	private List<String> wordsList = null;
	private List<String> wordConfidenceList = null;
	private String fileName = null;


	

}
