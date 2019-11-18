/**
 * 
 */
package com.c3trTranscibe.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.domain.Users;

/**
 * @author rajesh
 *
 */

public interface UsersRepository extends CrudRepository<Users, Long>{

	  @Query("select * from Users where upper(username) = upper(:username) ")
	  Optional<Users> findByUsername(String username);
	  
	  @Query("select * from Users where upper(emailAddress) = upper(:emailAddress) and  upper(username) = upper(:username)  ")
	  Optional<Users> findByUsernameAndEmail(String username);
	  
	  @Query("select * from Users where upper(emailAddress) = upper(:emailAddress) and  upper(username) = upper(:username)  and active=true ")
	  List<Users> findByEmailAddressAndusernameAndActive(String emailAddress, String username);
	  
	  @Query("select * from Users where upper(emailAddress) = upper(:emailAddress) and  upper(username) = upper(:username)  and active=false  ")
	  Optional<Users> findByEmailAddressAndUsernameAndInactive(String emailAddress, String username);
	  
	  
	  @Query("select * from Users where upper(emailAddress) = upper(:emailAddress) and  upper(username) = upper(:username)  and disabled=true  ")
	  Optional<Users> findByEmailAddressAndUsernameAndDisabled(String emailAddress, String username);
	  
	
}
