package com.rajesh.transcribe.transribeapi.repositories;

import com.rajesh.transcribe.transribeapi.TranscribeapiApplication;
import com.rajesh.transcribe.transribeapi.api.repository.AppUsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJdbcTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ContextConfiguration(classes = TranscribeapiApplication.class)
@ExtendWith(SpringExtension.class)
public class SampleRepoNonTransactionalTests {
    
    @Autowired
    private AppUsersRepository appUsersRepository;
    
    @Test
    public void testContext(){
        assertNotNull(appUsersRepository);
    }
}