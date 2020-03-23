/**
 * 
 */
package com.c3trTranscibe.springboot.services.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.domain.AppUsers;

/**
 * @author rajesh
 *
 */
@Service
public class AppEmailService {

	@Autowired
	Environment env;
	
	
	/**
	 * 
	 * @param userReq
	 * @return Boolean
	 */
	public Boolean sendPasswordRestEmail(AppUsers userReq) {
		
		return true;
	}
}
