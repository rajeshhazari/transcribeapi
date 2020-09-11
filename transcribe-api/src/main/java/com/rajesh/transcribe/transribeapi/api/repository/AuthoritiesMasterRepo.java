package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.AppUsersAuth;
import com.rajesh.transcribe.transribeapi.api.domian.AuthoritiesMaster;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


public interface  AuthoritiesMasterRepo extends CrudRepository<AuthoritiesMaster, Long> {

    @Query("select * from authorities_master where upper(email) = upper(:email)   ")
    AuthoritiesMaster findByRoleId(String roleId);

    @Query("select * from authorities_master where role_id in roleIds   ")
    List<AuthoritiesMaster> findByRolesId(List<String> roleIds);
}
