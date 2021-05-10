package com.c3transcribe.transribeapi.api.domian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("APPUSERS_TRANSCRIPTIONS")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserTranscriptions {

	
	@Id
	@Column(value = "log_id")
	public Long logId;
	@Column(value = "transcription_req_id")
	public Long transcriptionReqId;
	public String username;
	public String email;
	@Column(value = "transcription_res_type")
	public String transcribeResType;
	@Column(value = "file_name")
	public String fileName;
	@Column(value = "session_id")
	public String sessionId;
	public Long userid;
	public boolean transcribed ;
	public boolean  downloaded ;
	//VARCHAR(20) default 'application/json'
	//TODO define the most popular format as enum in Java and DB
	@Column(value = "transcribe_res_available_format")
	public String   transcribeResAvailableFormat ;
	@Column(value = "transcribe_res_downloaded_format")
	public String   transcribeResDownloadedFormat;
}
/* enum class TranscibeResponseTypes {
	application/json, application/text,application/pdf
}*/

