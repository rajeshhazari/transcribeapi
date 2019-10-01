/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.model.TranscriptionResponse;

import edu.cmu.sphinx.api.Configuration;

/**
 * @author rajesh
 *
 */
@Service
public class TranscriptionService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	Environment env;
	@Autowired
	Configuration configuration;
	
	
	public TranscriptionResponse transcribeAudio(File audioFile) {
		
		return null;
	}

	public TranscriptionResponse transcribeVideo(File videoFile) {
			
			return null;
		}
}
