package com.c3transcribe.transcribeapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJdbcRepositories("com.rajesh.transcribe.transribeapi.api")
@ComponentScan({"com.rajesh.transcribe.*","com.c3transcribe.core"})
public class TranscribeApiApplication extends SpringBootServletInitializer  {
//implements ApplicationContextInitializer<ContextRefreshedEvent> {

	private final Logger logger = LoggerFactory.getLogger(TranscribeApiApplication.class);


	/*@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(8080);
		return connector;
	}*/

	public static void main(String[] args) throws Exception {
		new TranscribeApiApplication().configure(new SpringApplicationBuilder(TranscribeApiApplication.class))
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
