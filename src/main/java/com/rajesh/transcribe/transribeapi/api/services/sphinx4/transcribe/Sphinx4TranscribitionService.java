/**
 * 
 */
package com.rajesh.transcribe.transribeapi.api.services.sphinx4.transcribe;

import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.domian.TranscribeFileLog;
import com.rajesh.transcribe.transribeapi.api.domian.UserTranscriptions;
import com.rajesh.transcribe.transribeapi.api.models.sphinx4.TranscribtionResponseDto;
import com.rajesh.transcribe.transribeapi.api.repository.AppUsersRepository;
import com.rajesh.transcribe.transribeapi.api.repository.UserTranscribtionsRepository;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * @author rajesh
 *
 */

@Service
public class Sphinx4TranscribitionService {

	private Logger logger = LoggerFactory.getLogger(Sphinx4TranscribitionService.class);

	@Autowired
    Environment env;

	@Autowired
    Configuration sphinxConfiguration;
	
	@Autowired
	Executor asyncExecutor;
	
	@Autowired
	UserTranscribtionsRepository userTranscribtionsRepository;
	@Autowired
	private AppUsersRepository appUsersRepo;
	
	/**
	 * 
	 * @param file
	 * @param transcribtionReqId
	 * @param token
	 * @param userEmail
	 * @param  sessionId
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public TranscribtionResponseDto transribeAudioforText(File file, String transcribtionReqId, final String token, final String userEmail,final @NotNull @NotBlank String sessionId, final Long size) throws IOException, ExecutionException{
		
		SimpleAsyncTaskExecutor delegateExecutor =
				new SimpleAsyncTaskExecutor();
		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(sphinxConfiguration);
		InputStream stream = new FileInputStream(file);
		TranscribtionResponseDto rdto = new TranscribtionResponseDto();
		recognizer.startRecognition(stream);
		rdto = extractTranscribedTextFromSpeechRecognizer(recognizer, transcribtionReqId);
		rdto.setToken(token);
		rdto.setFileName(file.getName());
		final String transcribedText = rdto.getTranscribedText();
		delegateExecutor.execute(() -> {
			UserTranscriptions userTranscriptions = new UserTranscriptions();
			userTranscriptions.setEmail(userEmail);
			userTranscriptions.setFileName(file.getName());
			userTranscriptions.setSessionId("1234");
			userTranscriptions.setTranscriptionReqId(Long.parseLong(transcribtionReqId));
			userTranscriptions.setTranscribeResAvailableFormat("application/json");
			userTranscriptions.setTranscribed(true);
			userTranscriptions.setDownloaded(false);
			Optional<AppUsers> appUsers = appUsersRepo.findByEmail(userEmail);
			if(Objects.nonNull(appUsers.get())){
				userTranscriptions.setUsername(appUsers.get().getUsername());
				userTranscriptions.setUserid(appUsers.get().getUserid());
			}
			TranscribeFileLog tflog = new TranscribeFileLog();
			tflog.setFileName(file.getName());
			tflog.setTReqId(Long.parseLong(transcribtionReqId));
			tflog.setLogId(userTranscriptions.getLogId());
			tflog.setEmail(userEmail);
			tflog.setFileSize(size);
			tflog.setSessionId(sessionId);
			tflog.setTRes(transcribedText);
			userTranscriptions.setTranscribeResType("application/JSON");
			userTranscriptions.setTranscribeFileLog(tflog);
			userTranscribtionsRepository.save(userTranscriptions);
			//Long id = tfLogRepo.save(tflog).getId();
			logger.debug("transcribefile  for requestId {} is saved", transcribtionReqId);
		});
		
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		//return extractTranscribedTextFromSpeechRecognizer(recognizer, transcribtionReqId);
		//TranscribtionResponseDto rDto = responseDto.
		return rdto;
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
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws NumberFormatException 
	 */
	public TranscribtionResponseDto transcribeAudio(File audioFile, @NotNull @NotBlank String reqId) throws  IOException, ExecutionException {

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(sphinxConfiguration);
		InputStream stream = new FileInputStream(audioFile);
		recognizer.startRecognition(stream);
		
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		//wordsList = getWordsList(result.getWords());
		return extractTranscribedTextFromSpeechRecognizer(recognizer, reqId);
	}

	

	/**
	 * Transcribes Video file
	 * 
	 * @param videoFile
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public TranscribtionResponseDto transcribeVideo(File videoFile, String reqId) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(sphinxConfiguration);
		InputStream stream = new FileInputStream(videoFile);
		recognizer.startRecognition(stream);
		
		return  extractTranscribedTextFromSpeechRecognizer(recognizer, reqId);
	}
	
	/**
	 *
	 * @param words
	 * @return
	 */
	@Async("appAsyncExecutor")
	private List<String> getWordsList(List<WordResult> words) {
		List<String> wordsList = new ArrayList<>();
		words.forEach(ele -> wordsList.add(ele.getWord().toString()));
		return wordsList;
	}
	
	/**
	 *
	 * @param words
	 * @return
	 */
	@Async("appAsyncExecutor")
	private List<String> getWordsConfidenceDetails(List<WordResult> words) {
		List<String> wordsList = new ArrayList<>();
		words.stream().forEach(ele -> wordsList.add(ele.toString()));
		return wordsList;
	}
	
	
	/**
	 *
	 * @param recognizer
	 * @param reqId
	 * @return TranscribtionResponseDto
	 */
	@Async
	private TranscribtionResponseDto extractTranscribedTextFromSpeechRecognizer(StreamSpeechRecognizer recognizer, String reqId) {
		
		StringBuilder unformattedTranscribeText = new StringBuilder();
		SpeechResult result;
		List<String> wordsList = null;
		List<String> wordConfList = null;
        while ((result = recognizer.getResult()) != null) {
        	//logger.debug("Hypothesis: {}\n", result.getHypothesis());
			unformattedTranscribeText.append(result.getHypothesis());
			unformattedTranscribeText.append(" ");
			wordsList = getWordsList(result.getWords());
			wordConfList = getWordsConfidenceDetails(result.getWords());
			logger.debug("Best 3 hypothesis:");
            for (String s : result.getNbest(3))
            	logger.debug("Hypothesis: {}\n", s);

			
		}
        recognizer.stopRecognition();
        
        return new TranscribtionResponseDto(unformattedTranscribeText.toString(), reqId, null, false, wordsList,null,null);
	}
}

