package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisteredUsersRepo extends CrudRepository<RegisteredUserVerifyLogDetials, Integer> {
	
	List<RegisteredUserVerifyLogDetials> findByEmailAndCode(@Param("email") String email, @Param("code") Integer code);
	List<RegisteredUserVerifyLogDetials> findByEmail(@Param("email") String email);
}
