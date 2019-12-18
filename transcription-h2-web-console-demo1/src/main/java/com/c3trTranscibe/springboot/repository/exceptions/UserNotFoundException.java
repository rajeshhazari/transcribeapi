package com.c3trTranscibe.springboot.repository.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends Exception {
	// ~ Constructors
	// ===================================================================================================

	/**
	 * Constructs a <code>UsernameNotFoundException</code> with the specified message.
	 *
	 * @param msg the detail message.
	 */
	public UserNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@code UsernameNotFoundException} with the specified message and root
	 * cause.
	 *
	 * @param msg the detail message.
	 * @param t root cause
	 */
	public UserNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}