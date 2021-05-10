/** */
package com.c3transcribe.transcribeapi.module.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/** @author rajesh */
@Configuration
@EnableAutoConfiguration(exclude = { H2ConsoleAutoConfiguration.class})
@EnableConfigurationProperties(ApiModuleProperties.class)
public class TranscribeApiModuleConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscribeApiModuleConfig.class);
	@Autowired DataSource dataSource;
	private static final String USER_CONSTRAINT = "CONFIDENTIAL";
	private static final String HTTP = "http";
	
	@Value("${server.port}")
	private int port;
	@Value("${server.http.port}")
	private int httpPort;
	
	@Value("${server.servlet.contextPath}")
	private String context;

	private DataSourceHealthIndicator dataSourceHealthIndicator;

  /*@Bean
  @Profile("default")
  public DataSource createH2DataSource() {
  EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
  	     .setType(EmbeddedDatabaseType.H2)
  	     .setScriptEncoding("UTF-8")
  	     .ignoreFailedDrops(true)
  	     .addScripts(schemaScript,inputDataScript)
  	     .build();
  return db;
  }*/

  
  /*@Bean
  @Profile("default")
  public DataSource embeddedDataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:/h2-db/schema_h2.sql")
        .addScript("classpath:/h2-db/data-temp.sql")
        .setScriptEncoding("UTF-8")
        .ignoreFailedDrops(true)
        .continueOnError(true)
        .build();
  }*/
	

	@Bean
	PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public  DataSourceHealthIndicator dataSourceHealthIndicator(){
		return   new DataSourceHealthIndicator(dataSource);
	}


	@Bean(name = "sphinxConfiguration")
	public edu.cmu.sphinx.api.Configuration getAudioTranscribeConfiguration() {
		edu.cmu.sphinx.api.Configuration configuration = new edu.cmu.sphinx.api.Configuration();
		
		configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");
		
		LOGGER.debug("sphinxConfiguration:: status:: ", configuration);
		LOGGER.debug("sphinxConfiguration:: Language MOdel:: ", configuration.getLanguageModelPath());
		LOGGER.debug(
				"sphinxConfiguration:: AcousticModelPath:: ", configuration.getAcousticModelPath());
		LOGGER.debug("sphinxConfiguration:: DictionaryPath:: ", configuration.getDictionaryPath());
		LOGGER.debug("sphinxConfiguration:: GrammerPath:: ", configuration.getGrammarPath());
		LOGGER.debug("sphinxConfiguration:: GrammerName:: ", configuration.getGrammarName());
		
		return configuration;
	}
	
	@Bean(name = "secureRandom")
	public SecureRandom createRandom() throws NoSuchAlgorithmException {
		return SecureRandom.getInstance("SHA1PRNG");
	}
	
	
	@Bean
	public JavaMailSender javaMailSender(){
		return  new JavaMailSenderImpl();
	}
	
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint(USER_CONSTRAINT);
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(redirectConnector());
		return tomcat;
	}
	
	private Connector redirectConnector() {
		Connector connector = new Connector(
				TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme(HTTP);
		connector.setPort(httpPort);
		connector.setSecure(false);
		connector.setRedirectPort(port);
		return connector;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
  /*
  @Bean
  public SessionRegistry getSessionRegistry() {
  	return new SessionRegistryImpl();
  }

  @Bean
  public SessionAuthenticationStrategy getSessionAuthStrategy(SessionRegistry sessionRegistry) {
  	ConcurrentSessionControlAuthenticationStrategy controlAuthenticationStrategy =
  			new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);

  	return controlAuthenticationStrategy;
  }

@Bean
       public ApplicationListener<?> loggingListener() {

               return (ApplicationListener<ApplicationEvent>) event -> {
                       if (event instanceof RelationalEvent) {
                               System.out.println("Received an event: "  event);
                       }
               };
       }

       /*
        * @return {@link BeforeSaveCallback} for {@link Category}.
        /
       @Bean
       public BeforeSaveCallback<Category> timeStampingSaveTime() {

               return (entity, aggregateChange) -> {

                       entity.timeStamp();

                       return entity;
               };


  */
	
}
