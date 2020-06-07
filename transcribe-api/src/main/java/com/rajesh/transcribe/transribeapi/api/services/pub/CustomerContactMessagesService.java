package com.rajesh.transcribe.transribeapi.api.services.pub;

import com.rajesh.transcribe.transribeapi.api.domian.CustomerContactMessages;
import com.rajesh.transcribe.transribeapi.api.models.dto.CustomerContactMessagesDto;
import com.rajesh.transcribe.transribeapi.api.repository.CustomerContactMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class CustomerContactMessagesService {
    
    @Autowired
    CustomerContactMessagesRepository customerContactMessagesRepository;
    
    public boolean saveCustomerMessage(final CustomerContactMessagesDto customerContactMessagesDto) {
        CustomerContactMessages c = new CustomerContactMessages();
        c.setEmail(customerContactMessagesDto.getEmail());
        c.setFirstName(customerContactMessagesDto.getFirstName());
        c.setLastName(customerContactMessagesDto.getLastName());
        c.setMessage(c.getMessage());
        return Objects.nonNull(customerContactMessagesRepository.save(c));
    }
}
