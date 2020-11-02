package com.c3transcribe.transcribeapi.api.repository;

import com.c3transcribe.transcribeapi.api.domian.CustomerContactMessages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerContactMessagesRepository extends CrudRepository<CustomerContactMessages, Long> {
}
