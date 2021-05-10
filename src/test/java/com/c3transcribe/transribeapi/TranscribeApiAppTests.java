package com.c3transcribe.transribeapi;

import com.c3transcribe.transribeapi.api.controller.AppInfoController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("dev-test")
class TranscribeApiAppTests {
	
	@Autowired
	private AppInfoController controller;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}

}
