/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.model.dto.search;

import com.rajesh.transcribe.transribeapi.api.model.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rhazari
 *
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class SearchDetails extends BaseResponseDto {
	
	private String status = null;
	private String searchDate = null;
	private String totalCount = null;
	private String pageNumber = null;
	private String limit = null;
	private String responseTime = null;
	
}