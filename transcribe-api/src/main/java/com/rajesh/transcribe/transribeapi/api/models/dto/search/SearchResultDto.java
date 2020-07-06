/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.models.dto.search;

import com.rajesh.transcribe.transribeapi.api.models.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author rhazari
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto extends BaseResponseDto {
    
    private String sttTranscriptionModule = null;
    private String TranscriptionReqId = null;
    private String fileName = null;
    private Long fileSize = null;
    private String TranscriptionResp = null;
    private String OriginalMediaFormat = null;
    private String mediaFormat = null;
    private Date transcribedDate = null;
    private Date downloadedDate = null;
    private String duration = null;
    private Date lastModified = null;
    private Map<String, Long> spokenTextMap;
}
