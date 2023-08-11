/**
 *
 */
package com.rajesh.transcribe.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rajesh
 *
 */
public class SamplePlainAuthenticationExample {
	private static AuthenticationManager am = new SampleAuthenticationManager();
	private static final Logger logger = LoggerFactory.getLogger(SampleAuthenticationManager.class);


	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			System.out.println("Please enter your username:");
			String name = in.readLine();
			System.out.println("Please enter your password:");
			String password = in.readLine();
			try {
				Authentication request = new UsernamePasswordAuthenticationToken(name, password);
				Authentication result = am.authenticate(request);
				SecurityContextHolder.getContext().setAuthentication(result);
				break;
			} catch(AuthenticationException e) {
				System.out.println("Authentication failed: " + e.getMessage());
			}
		}
		System.out.println("Successfully authenticated. Security context contains: " +
				SecurityContextHolder.getContext().getAuthentication());
	}
}

class SampleAuthenticationManager implements AuthenticationManager {
	static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	static {
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
		AUTHORITIES.add(new SimpleGrantedAuthority("SUPER_USER"));
		AUTHORITIES.add(new SimpleGrantedAuthority("ADMIN"));
		AUTHORITIES.add(new SimpleGrantedAuthority("SUPER_ADMIN"));
	}

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if (auth.getName().equals(auth.getCredentials())) {
			return new UsernamePasswordAuthenticationToken(auth.getName(),
					auth.getCredentials(), AUTHORITIES);
		}
		throw new BadCredentialsException("Bad Credentials");
	}
}

	

