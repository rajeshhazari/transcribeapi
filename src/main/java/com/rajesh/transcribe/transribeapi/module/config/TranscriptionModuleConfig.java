/**
 * 
 */
package com.rajesh.transcribe.transribeapi.module.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author rajesh
 *
 */
@Configuration
@EnableJdbcRepositories("com.rajesh.transcribe.transribeapi.api.repository")
public class TranscriptionModuleConfig{

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptionModuleConfig.class);
	

	  @Value("${server.port}")
	  private int port;
	  
	  @Value("${server.servlet.contextPath}")
	  private String context;
	  
	  @Value("${spring.datasource.schema}")
	  private String schemaScript;
	  
	  @Value("${spring.datasource.data}")
	  private String inputDataScript;
	  
	  @Autowired
	  DataSource dataSource;
	  
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
	
	@Bean
	@Profile({"dev", "qa" , "prod"})
	public DataSource getDataSource() {
		return  dataSource;
	}
	
	@Bean
    NamedParameterJdbcOperations operations() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name = "sphinxConfiguration")
	public edu.cmu.sphinx.api.Configuration getAudioTranscribeConfiguration() {
		edu.cmu.sphinx.api.Configuration configuration = new edu.cmu.sphinx.api.Configuration();

        configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");
        
        LOGGER.debug("sphinxConfiguration:: status:: ", configuration);
		LOGGER.debug("sphinxConfiguration:: Language MOdel:: ", configuration.getLanguageModelPath());
		LOGGER.debug("sphinxConfiguration:: AcousticModelPath:: ", configuration.getAcousticModelPath());
		LOGGER.debug("sphinxConfiguration:: DictionaryPath:: ", configuration.getDictionaryPath());
		LOGGER.debug("sphinxConfiguration:: GrammerPath:: ", configuration.getGrammarPath());
		LOGGER.debug("sphinxConfiguration:: GrammerName:: ", configuration.getGrammarName());
		
		return configuration;
	}

	

	@Bean(name = "secureRandom")
	public SecureRandom createRandom () throws NoSuchAlgorithmException {
		return SecureRandom.getInstance("SHA1PRNG");
		
	}
	
	 /*@Bean(name = "asyncExecutor")
	    public Executor asyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(5);
	        executor.setMaxPoolSize(5);
	        executor.setQueueCapacity(100);
	        executor.setThreadNamePrefix("AsynchThread-");
	        executor.initialize();
	        return executor;
	    }*/
	  
	  
	 /* @Bean
	  public TomcatServletWebServerFactory servletContainer(){
	      TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(context,port);
	      LOGGER.info("Setting custom configuration for Mainstay:");
	      LOGGER.info("Setting port to {}",port);
	      LOGGER.info("Setting context to {}",context);
	      //factory.setErrorPages(pageHandlers);
	      return factory;
	  }*/
	
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	  
}
