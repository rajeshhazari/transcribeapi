/**
 * 
 */
package com.c3trTranscibe.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.domain.RegisteredUser;
import org.springframework.stereotype.Repository;

/**
 * @author rajesh
 *
 */
/*@Repository
public interface RegisteredUsersRepository extends CrudRepository<RegisteredUser, Long>{
	
	RegisteredUser findByEmail(String email);
	RegisteredUser findByUsername(String username);
}*/
@Deprecated
public interface RegisteredUsersRepository{}