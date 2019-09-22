/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author rajesh
 *
 */
public class SampleMessageService {

	

	@PreAuthorize("authenticated")
	public String getMessage() {
		Authentication authentication = SecurityContextHolder.getContext()
															 .getAuthentication();
		return "Hello " + authentication;
	}
	
}
