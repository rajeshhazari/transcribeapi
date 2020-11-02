/**
 * 
 */
package com.c3trTranscribe.springboot.test.repository.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.c3transcribe.transcribeapi.TranscribeapiApplication;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author rajesh
 *
 */
@ContextConfiguration(classes = TranscribeapiApplication.class)
public class TranscriptionModuleConfigTest  {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptionModuleConfigTest.class);
	

	  @Value("${server.port}")
	  private int port;
	  
	  @Value("${server.servlet.contextPath}")
	  private String context;
	  
	  @Value("${spring.datasource.schema}")
	  private String schemaScript;
	  
	  @Value("${spring.datasource.schema}")
	  private String inputDataScript;
	  
	  @Autowired
	private DataSource dataSource;
	  
	/*@Bean
	public DataSource getH2DataSource() {
	EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
		     .generateUniqueName(true)
		     .setType(EmbeddedDatabaseType.H2)
		     .setScriptEncoding("UTF-8")
		     .ignoreFailedDrops(true)
		     .addScript(schemaScript)
		     .addScripts(inputDataScript)
		     .build();
	return db;
	}*/
	
	/*
	@Bean
    NamedParameterJdbcOperations operations() { 
        return new NamedParameterJdbcTemplate(getH2DataSource());
    }*/

    /*@Bean
    PlatformTransactionManager transactionManager() { 
        return new DataSourceTransactionManager(getH2DataSource());
	}*/
	
	@Bean
	public edu.cmu.sphinx.api.Configuration getAudioTranscribeConfiguration() {
		edu.cmu.sphinx.api.Configuration configuration = new edu.cmu.sphinx.api.Configuration();

        configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");
        return configuration;
	}

	

	@Bean(name = "secureRandom")
	public SecureRandom createRandom () throws NoSuchAlgorithmException {
		return SecureRandom.getInstance("SHA1PRNG");
		
	}
	
	 @Bean(name = "asyncExecutor")
	    public Executor asyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(5);
	        executor.setMaxPoolSize(5);
	        executor.setQueueCapacity(100);
	        executor.setThreadNamePrefix("AsynchThread-");
	        executor.initialize();
	        return executor;
	    }
	 
	
	
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	  
}
