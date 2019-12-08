/**
 * 
 */
package com.c3trTranscibe.springboot.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author rajesh
 *
 */
@Configuration
@EnableJdbcRepositories("com.c3trTranscibe.springboot")
@EnableAsync
public class TranscriptionModuleConfig {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptionModuleConfig.class);
	

	  @Value("${server.port}")
	  private int port;
	  
	  @Value("${server.servlet.contextPath}")
	  private String context;
	  
	@Bean
	public DataSource getH2DataSource() {
	EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
		     .generateUniqueName(true)
		     .setType(EmbeddedDatabaseType.H2)
		     .setScriptEncoding("UTF-8")
		     .ignoreFailedDrops(true)
		     .addScript("h2-db/tables_h2.sql")
		     .addScripts("h2-db/data-temp.sql")
		     .build();
	return db;
	}
	
	
	@Bean
    NamedParameterJdbcOperations operations() { 
        return new NamedParameterJdbcTemplate(getH2DataSource());
    }

    @Bean
    PlatformTransactionManager transactionManager() { 
        return new DataSourceTransactionManager(getH2DataSource());
	}
	
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
	 
	 private Set<ErrorPage> pageHandlers;
	  @PostConstruct
	  private void init(){
	      pageHandlers = new HashSet<ErrorPage>();
	      pageHandlers.add(new ErrorPage(HttpStatus.NOT_FOUND,"/notfound.html"));
	      pageHandlers.add(new ErrorPage(HttpStatus.FORBIDDEN,"/forbidden.html"));
	  }
	  
	  
	  @Bean
	  public TomcatServletWebServerFactory servletContainer(){
	      TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(context,port);
	      LOGGER.info("Setting custom configuration for Mainstay:");
	      LOGGER.info("Setting port to {}",port);
	      LOGGER.info("Setting context to {}",context);
	      factory.setErrorPages(pageHandlers);
	      return factory;
	  }
	  
}
