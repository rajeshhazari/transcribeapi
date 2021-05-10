package com.c3transcribe.transribeapi;

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
//@PropertySource("classpath:META-INF/build-info.properties")
@ComponentScan("com.c3transcribe.*")
public class TranscribeApiApp extends SpringBootServletInitializer  {
	
	private final Logger logger = LoggerFactory.getLogger(TranscribeApiApp.class);
	
	public static void main(String[] args) throws Exception {
		new TranscribeApiApp().configure(new SpringApplicationBuilder(TranscribeApiApp.class))
				.bannerMode(Banner.Mode.OFF)
				.web(WebApplicationType.SERVLET)
				.logStartupInfo(true)
				.run(args);
	}

}
