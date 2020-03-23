package com.c3trTranscribe.springboot.test.auth;

import com.c3trTranscibe.springboot.domain.AppUsers;
import com.c3trTranscibe.springboot.repository.AppusersRepository;
import com.c3trTranscribe.springboot.test.repository.config.TranscriptionModuleConfigTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranscriptionModuleConfigTest.class)
@ActiveProfiles("default")public class PasswordEnconderGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordEnconderGenerator.class);
    
    
    @Autowired
    private AppusersRepository userDAO;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Test
    public void testPasswordMatch() {
        
        int i = 0;
        
            String password = "admin321";
            String hashedPassword = encoder.encode(password);
    
        List<AppUsers> user = userDAO.findByEmail("rajeshhazari@gmail.com");
    
        logger.info(hashedPassword);
            logger.info(Boolean.valueOf(encoder.matches(password, hashedPassword)).toString() + " iteration :: "+ i);
            i++;
        assertEquals(hashedPassword, user.get(0).getPassword());
    
    
    
    }
}
