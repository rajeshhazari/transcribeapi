/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.models.dto.search;

import com.rajesh.transcribe.transribeapi.api.models.dto.AppError;
import com.rajesh.transcribe.transribeapi.api.models.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author rhazari
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultList extends BaseResponseDto {
	
	private List<SearchResult> searchResult = null;
	private AppError error;

}
