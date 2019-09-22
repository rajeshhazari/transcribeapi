/**
 * 
 */
package com.c3trTranscibe.springboot.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author rajesh
 *
 */
@Configuration
public class TranscriptionModuleConfig {

	
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

}
