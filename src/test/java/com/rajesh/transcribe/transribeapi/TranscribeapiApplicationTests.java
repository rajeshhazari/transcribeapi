package com.rajesh.transcribe.transribeapi;

import com.rajesh.transcribe.transribeapi.api.controller.AppInfoController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TranscribeapiApplicationTests {
	
	@Autowired
	private AppInfoController controller;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}

}
