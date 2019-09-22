package com.c3trTranscibe.springboot.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("UsersTranscriptions")
public class UserTranscriptions {

	
	@Id
	public Long logId;
	public Long transcribeReqId;
	public String username;
	public String transcribeResType;
	public String fileName;
	public String sessionId;
	public Long userid;
	public boolean transcribed = false;
	public boolean  downloaded = false;
	//VARCHAR(10) default 'application/json'
	public String   transcribeResAvailableFormat ;
	public String   transcribeResDownloadedFormat; 
}
