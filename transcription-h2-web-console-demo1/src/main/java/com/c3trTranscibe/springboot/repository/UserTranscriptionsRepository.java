package com.c3trTranscibe.springboot.repository;

import com.c3trTranscibe.springboot.domain.AppUsers;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.domain.UserTranscriptions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTranscriptionsRepository  extends CrudRepository<UserTranscriptions, Long> {

    @Query("select * from UsersTranscriptions where upper(email) = upper(:email) and  upper(username) = upper(:username)  and transcription_req_id=:transcriptionReqId  ")
    List<AppUsers> findTranscriptionsByEmailAndUsername(String email, String username, String transcriptionReqId);
}
