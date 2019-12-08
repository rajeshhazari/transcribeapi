package com.c3trTranscibe.springboot.repository;

import com.c3trTranscibe.springboot.domain.Users;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.model.repository.UserTranscriptions;

import java.util.Optional;

public interface UserTranscriptionsRepository  extends CrudRepository<UserTranscriptions, Long> {

    @Query("select * from UsersTranscriptions where upper(email) = upper(:email) and  upper(username) = upper(:username)  and transcription_req_id=:transcriptionReqId  ")
    Optional<Users> findTranscriptionsByEmailAndUsername(String email, String username, String transcriptionReqId);
}
