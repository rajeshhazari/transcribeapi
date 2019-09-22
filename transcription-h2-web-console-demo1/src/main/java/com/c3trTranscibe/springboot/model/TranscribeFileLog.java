package com.c3trTranscibe.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rajesh
 *
 */
@Table("transcribefilelog")
public class TranscribeFileLog {
		
		@Id
		public Long logId;
		public Long transcribeReqId;
		public String username;
		public String transcribeResType;
		public String fileName;
		public String sessionId;
	    



	  public Long getTranscribeReqId() {
		return transcribeReqId;
	}

	public void setTranscribeReqId(Long transcribeReqId) {
		this.transcribeReqId = transcribeReqId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserName(){
	    return this.username;
	  }

	  public void setUserName(String username){
	    this.username = username;
	  }

	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	}
