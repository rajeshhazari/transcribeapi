package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;
import org.springframework.data.repository.CrudRepository;

public interface RegisteredUsersRepo extends CrudRepository<RegisteredUserVerifyLogDetials, Integer> {
	
	//RegisteredUserVerifyLogDetials findByEmail(String email);
	//RegisteredUserVerifyLogDetials SaveConfEmailUrl(String email);
}
