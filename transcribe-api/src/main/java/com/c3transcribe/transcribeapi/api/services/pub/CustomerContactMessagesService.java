package com.c3transcribe.transcribeapi.api.services.pub;

import com.c3transcribe.transcribeapi.api.domian.CustomerContactMessages;
import com.c3transcribe.transcribeapi.api.models.dto.CustomerContactMessagesDto;
import com.c3transcribe.transcribeapi.api.repository.CustomerContactMessagesRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class CustomerContactMessagesService {
    private static final Logger logger = getLogger(CustomerContactMessagesService.class);
    
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
