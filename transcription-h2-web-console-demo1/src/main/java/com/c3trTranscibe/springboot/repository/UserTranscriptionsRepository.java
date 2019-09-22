package com.c3trTranscibe.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.model.repository.UserTranscriptions;

public interface UserTranscriptionsRepository  extends CrudRepository<UserTranscriptions, Long> {
	
}
