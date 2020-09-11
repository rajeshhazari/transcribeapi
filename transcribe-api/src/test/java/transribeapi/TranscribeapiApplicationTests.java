package transribeapi;

import com.rajesh.transcribe.transribeapi.TranscribeapiApplication;
import com.rajesh.transcribe.transribeapi.api.controller.AppInfoController;
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
