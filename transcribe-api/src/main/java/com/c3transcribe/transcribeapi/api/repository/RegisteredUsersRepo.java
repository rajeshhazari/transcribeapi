package com.c3transcribe.transcribeapi.api.repository;

import com.c3transcribe.transcribeapi.api.domian.RegisteredUserVerifyLogDetials;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisteredUsersRepo extends CrudRepository<RegisteredUserVerifyLogDetials, Integer> {
	
	@Query("select * from USERREGVERIFYLOGDETAILS where email = :email and code = :code ")
	List<RegisteredUserVerifyLogDetials> findByEmailAndCode(@Param("email") String email, @Param("code") Integer code);

	@Query("select * from USERREGVERIFYLOGDETAILS where email = :email ")
	List<RegisteredUserVerifyLogDetials> findByEmail(@Param("email")  String email);
    
    //List<RegisteredUserVerifyLogDetials> findByEmailAndCode(String email, String code);
	
	@Query("select * from USERREGVERIFYLOGDETAILS where email = :email and confEmailToken = :confEmailToken ")
	RegisteredUserVerifyLogDetials findByEmailAndConfEmailToken(@Param("email") String email, @Param("confEmailToken")  String confEmailToken);
}
