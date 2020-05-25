/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.model.dto.search;

import com.rajesh.transcribe.transribeapi.api.model.dto.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author rhazari
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult extends BaseResponseDto {
    
    private String sttTranscriptionModule = null;
    private String transcribtionReqId = null;
    private String email;
    private String fileName = null;
    private Long fileSize = null;
    private String transcribtionResp = null;
    private String OriginalMediaFormat = null;
    private String mediaFormat = null;
    private Date transcribedDate = null;
    private Date downloadedDate = null;
    private String duration = null;
    private Date lastModified = null;
}
