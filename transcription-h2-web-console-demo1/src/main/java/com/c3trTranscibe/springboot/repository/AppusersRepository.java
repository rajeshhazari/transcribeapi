/**
 * 
 */
package com.c3trTranscibe.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.domain.AppUsers;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author rajesh
 *
 */
@Repository
public interface AppusersRepository extends CrudRepository<AppUsers, Long>  {

	  /*@Query("select * from APPUSERS where upper(username) = upper(:username) ")
	  Optional<AppUsers> findByUsername(@Param("username") String username);
	  
	  @Query("select * from APPUSERS where upper(email) = upper(:email) and  upper(username) = upper(:username)  ")
	  Optional<AppUsers> findByUsernameAndEmail(@Param("email")  String email, @Param("username")  String username);

	@Query("select * from APPUSERS where upper(email) = upper(:email)   ")
	Optional<AppUsers> findByEmail(@Param("email")  String email);
	  
	  @Query("select * from APPUSERS where upper(email) = upper(:email) and  upper(username) = upper(:username)  and active=true ")
	  List<AppUsers> findByEmailAndusernameAndActive(@Param("email") String email, @Param("username") String username);
	  
	  @Query("select * from APPUSERS where upper(email) = upper(:email)  and active=false  ")
	  Optional<AppUsers> findByEmailAndInactive(@Param("email") String email);
	  
	  
	  @Query("select * from APPUSERS where upper(email) = upper(:email) and  upper(username) = upper(:username)  and disabled=true  ")
	  Optional<AppUsers> findByEmailAndUsernameAndDisabled(@Param("email") String email, @Param("username") String username);
*/
	
	List<AppUsers> findByEmail(String email);
	Optional<AppUsers> findByUserName(String username);
	Optional<AppUsers> findByEmailAndInactive(String email);
	Optional<AppUsers> 	findByUsernameAndEmail(String username, String email);
	Optional<AppUsers> findByUsername(String username);

	  
	
}
