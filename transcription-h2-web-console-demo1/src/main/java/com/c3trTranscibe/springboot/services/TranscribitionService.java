/**
 * 
 */
package com.c3trTranscibe.springboot.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.c3trTranscibe.springboot.model.TranscriptionResponse;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

/**
 * @author rajesh
 *
 */

@Service
public class TranscribitionService {

	private Logger logger = LoggerFactory.getLogger(TranscribitionService.class);

	@Autowired
	Environment env;

	@Autowired
	Configuration configuration;

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public TranscriptionResponse transribeAudioforText(File file, String transcribtionReqId) throws Exception{


		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = new FileInputStream(file);
		recognizer.startRecognition(stream);
		SpeechResult result;
		List<String> wordList = new ArrayList<>();
		String unformatted_transcribe_text = "";
		while ((result = recognizer.getResult()) != null) {
			logger.debug("Hypothesis: %s\n", result.getHypothesis());
			unformatted_transcribe_text = result.getHypothesis();
			 
		}
		result.getWords().stream().forEach(wordresult -> wordList.add(wordresult.getWord().toString()));
		recognizer.stopRecognition();
		recognizer = null;
		if(null == transcribtionReqId) {
			transcribtionReqId = String.valueOf(generateSecureRandomLong());
		}
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		return new TranscriptionResponse(unformatted_transcribe_text, Long.valueOf(transcribtionReqId), null, false, wordList);
	}


	
	/**
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */

	public   Long generateSecureRandomLong() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");

		// Get 128 random bytes
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);

		//Get random integer
		int r = secureRandomGenerator.nextInt();

		//Get random integer in range
		return secureRandomGenerator.nextLong();
	}
	
	
	public  Integer generateSecureRandomInt() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");

		// Get 128 random bytes
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);

		//Get random integer in range
		return secureRandomGenerator.nextInt();
	}
	
	/**
	 * Transcribes Audio with out an transcribeReqId
	 * 
	 * @param audioFile
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	public TranscriptionResponse transcribeAudio(File audioFile) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = new FileInputStream(audioFile);
		recognizer.startRecognition(stream);
		SpeechResult result;
		while ((result = recognizer.getResult()) != null) {
			logger.debug("Hypothesis: %s\n", result.getHypothesis());
		}
		String unformatted_transcribe_text = result.getHypothesis();
		recognizer.stopRecognition();
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		return new TranscriptionResponse(unformatted_transcribe_text, Long.valueOf(generateSecureRandomLong()), null, false, null);
	}

	
	/**
	 * Transcribe Video file
	 * 
	 * @param videoFile
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public TranscriptionResponse transcribeVideo(File videoFile) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = new FileInputStream(videoFile);
		recognizer.startRecognition(stream);
		SpeechResult result;
		while ((result = recognizer.getResult()) != null) {
			logger.debug("Hypothesis: %s\n", result.getHypothesis());
		}
		String unformatted_transcribe_text = result.getHypothesis();
		recognizer.stopRecognition();
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		return new TranscriptionResponse(unformatted_transcribe_text, Long.valueOf(generateSecureRandomLong()), null, false, null);
	}
}

