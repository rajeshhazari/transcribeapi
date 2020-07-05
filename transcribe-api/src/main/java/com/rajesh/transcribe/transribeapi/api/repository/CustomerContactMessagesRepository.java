package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.CustomerContactMessages;
import com.rajesh.transcribe.transribeapi.api.models.dto.CustomerContactMessagesDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerContactMessagesRepository extends CrudRepository<CustomerContactMessages, Long> {
}
