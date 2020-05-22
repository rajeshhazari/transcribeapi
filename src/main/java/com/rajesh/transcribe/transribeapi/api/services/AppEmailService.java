package com.rajesh.transcribe.transribeapi.api.services;

import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AppEmailService {

	@Autowired
	Environment env;
	
	
	/**
	 * 
	 * @param userReq
	 * @return Boolean
	 */
	public Boolean sendPasswordRestEmail(RegisteredUserVerifyLogDetials userReq) {
		
		return true;
	}
}
