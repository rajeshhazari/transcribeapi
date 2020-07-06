package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.UserTranscriptions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTranscriptionsRepository extends CrudRepository<UserTranscriptions, Long> {
}
