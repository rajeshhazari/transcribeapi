/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.models.dto.search;

import com.rajesh.transcribe.transribeapi.api.models.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author rhazari
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchDetails extends BaseResponseDto {
	
	private String status = null;
	private String searchDate = null;
	private String totalCount = null;
	private String pageNumber = null;
	private String limit = null;
	private String responseTime = null;
	
}
