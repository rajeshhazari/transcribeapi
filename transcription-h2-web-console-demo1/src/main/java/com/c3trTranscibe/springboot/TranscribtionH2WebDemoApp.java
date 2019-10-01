package com.c3trTranscibe.springboot;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.c3trTranscibe.*")
public class TranscribtionH2WebDemoApp extends SpringBootServletInitializer {

	
	private final Logger logger = LoggerFactory.getLogger(TranscribtionH2WebDemoApp.class);

	public static void main(String[] args) throws Exception {
		new TranscribtionH2WebDemoApp().configure(new SpringApplicationBuilder(TranscribtionH2WebDemoApp.class))
				.bannerMode(Banner.Mode.OFF).web(WebApplicationType.SERVLET).run(args);
	}
	// org.springframework.boot.web.servlet.context.
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			logger.debug("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			

		};
	}
}
