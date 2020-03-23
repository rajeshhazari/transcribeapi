/**
 * 
 */
package com.c3trTranscribe.springboot.test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.c3trTranscribe.springboot.test.repository.config.TranscriptionModuleConfigTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.c3trTranscibe.springboot.TranscribtionH2WebDemoApp;
import com.c3trTranscibe.springboot.domain.TranscribeFileLog;
import com.c3trTranscibe.springboot.repository.TranscribeFileLogRepository;

/**
 * @author rajesh
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranscriptionModuleConfigTest.class)
@ActiveProfiles("default")
public class TranscribeFileLogTest {
	
	private final Logger logger = LoggerFactory.getLogger(TranscribeFileLogTest.class);

	
	/*@Autowired
	TranscribeFileLogRepository repo;*/
	
	
	@Autowired
	Environment env;
	
	@Test
	public void getEnvironement() {
		assertNotNull(env);
		assertEquals(env.getProperty("spring.application.name"), "Transcribe Demo Api test");
	}
	
	
	@Test
	public void testFileLogSaveandCount() {
		TranscribeFileLog uploadFileLog = new TranscribeFileLog();
		uploadFileLog.setTranscribeReqId(1234L);
		uploadFileLog.setSessionId("1234Session");
		uploadFileLog.setUsername("Rajesh");
		uploadFileLog.setFileName("sample.wav");
		uploadFileLog.transcribeResType="application/json";
		/*repo.save(uploadFileLog);
		Assert.notNull(uploadFileLog);
		assertNotEquals(repo.count(),0);
		Optional<TranscribeFileLog> uploadFileLog1 = repo.findById(1234L);
		assertTrue(uploadFileLog.sessionId == uploadFileLog1.get().sessionId);
		assertEquals(uploadFileLog, uploadFileLog1.get());*/
	}
}
