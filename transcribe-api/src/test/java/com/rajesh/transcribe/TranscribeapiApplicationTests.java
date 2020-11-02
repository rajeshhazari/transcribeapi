package transribeapi;

import com.c3transcribe.transcribeapi.TranscribeapiApplication;
import com.c3transcribe.transcribeapi.api.controller.AppInfoController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Profile("dev-test")
@ContextConfiguration(classes = TranscribeapiApplication.class)
class TranscribeapiApplicationTests {
	
	@Autowired
	private AppInfoController controller;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}

}
