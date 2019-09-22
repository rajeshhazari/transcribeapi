/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.model.Users;

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
