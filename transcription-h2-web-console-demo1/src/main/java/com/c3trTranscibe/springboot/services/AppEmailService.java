/**
 * 
 */
package com.c3trTranscibe.springboot.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.domain.Users;

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
	public Boolean sendPasswordRestEmail(Users userReq) {
		
		return true;
	}
}
