package com.rajesh.transcribe.transribeapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.rajesh.transcribe.*")
public class TranscribeapiApplication extends SpringBootServletInitializer  {
//implements ApplicationContextInitializer<ContextRefreshedEvent> {
	
	private final Logger logger = LoggerFactory.getLogger(TranscribeapiApplication.class);
	
	public static void main(String[] args) throws Exception {
		new TranscribeapiApplication().configure(new SpringApplicationBuilder(TranscribeapiApplication.class))
				.bannerMode(Banner.Mode.OFF)
				.web(WebApplicationType.SERVLET)
				.logStartupInfo(true)
				.run(args);
	}

/*
     @Override
       public void initialize(final ContextRefreshedEvent contextRefreshedEvent) {
       logger.info("Application context is create successfully! {}",contextRefreshedEvent.getApplicationContext().getApplicationName());
 }*/
	
	
}
