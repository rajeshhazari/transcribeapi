/**
 * 
 */
package com.c3trTranscibe.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.model.repository.RegisteredUser;

/**
 * @author rajesh
 *
 */
public interface RegisteredUsersRepo extends CrudRepository<RegisteredUser, Long>{
	
	RegisteredUser findByEmail(String email);
	RegisteredUser findByUsername(String username);
}
