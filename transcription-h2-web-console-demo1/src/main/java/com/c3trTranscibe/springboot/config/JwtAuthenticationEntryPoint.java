/**
 * 
 */
package com.c3trTranscibe.springboot.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author rajesh
 *
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {


private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		LOGGER.debug("principal name :{}",request.getUserPrincipal().getName());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); 
	}
}
