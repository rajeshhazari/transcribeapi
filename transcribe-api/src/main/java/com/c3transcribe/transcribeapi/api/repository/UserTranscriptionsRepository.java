package com.c3transcribe.transcribeapi.api.repository;

import com.c3transcribe.transcribeapi.api.domian.AppUsers;
import com.c3transcribe.transcribeapi.api.domian.UserTranscriptions;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTranscriptionsRepository extends CrudRepository<UserTranscriptions, Long> {
    
    @Query("select * from UsersTranscriptions where upper(email) = upper(:email) and  upper(username) = upper(:username)  and transcription_req_id=:transcriptionReqId  ")
    Optional<UserTranscriptions> findTranscriptionsByEmailAndUsername(String email, String username, String transcriptionReqId);
}
