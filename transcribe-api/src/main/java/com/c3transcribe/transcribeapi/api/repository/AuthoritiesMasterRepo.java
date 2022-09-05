package com.c3transcribe.transcribeapi.api.repository;

import com.c3transcribe.transcribeapi.api.domian.AuthoritiesMaster;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface  AuthoritiesMasterRepo extends CrudRepository<AuthoritiesMaster, Long> {

    @Query("select * from authorities_master where upper(email) = upper(:email)   ")
    AuthoritiesMaster findByRoleId(String roleId);

    @Query("select * from authorities_master where role_id in :roleIds   ")
    List<AuthoritiesMaster> findByRolesId(List<String> roleIds);
}
