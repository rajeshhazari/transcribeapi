/**
 * 
 */
package com.c3transcribe.transcribeapi.api.services.sphinx4.transcribe;

import com.c3transcribe.transcribeapi.api.domian.AppUsers;
import com.c3transcribe.transcribeapi.api.domian.TranscribeFileLog;
import com.c3transcribe.transcribeapi.api.domian.UserTranscriptions;
import com.c3transcribe.transcribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import com.c3transcribe.transcribeapi.api.repository.AppUsersRepository;
import com.c3transcribe.transcribeapi.api.repository.UserTranscriptionsRepository;
import com.c3transcribe.transcribeapi.api.repository.exceptions.UserNotFoundException;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;
import org.apache.commons.lang3.StringUtils;
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

	@Autowired    private Environment env;
	@Autowired    private Configuration sphinxConfiguration;
	@Autowired	  private Executor asyncExecutor;
	@Autowired	  private UserTranscriptionsRepository userTranscriptionsRepository;
	@Autowired	  private AppUsersRepository appUsersRepo;
	
	/**
	 * 
	 * @param file
	 * @param transcriptionReqId
	 * @param token
	 * @param userEmail
	 * @param  sessionId
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public TranscriptionResponseDto transcribeAudioforText(File file, String transcriptionReqId, final String token,
														   final String userEmail, final @NotNull @NotBlank String sessionId,
														   final Long size) throws IOException, ExecutionException{
		
		TranscriptionResponseDto rdto = transcribeAudio(file,transcriptionReqId);
		rdto.setToken(token);
		rdto.setFileName(file.getName());
		final String transcribedText = rdto.getTranscribedText();
		asyncExecutor.execute(() -> {
			UserTranscriptions userTranscriptions = new UserTranscriptions();
			userTranscriptions.setEmail(userEmail);
			userTranscriptions.setFileName(file.getName());
			//TODO get unique user id or some cookie id
			StringUtils.firstNonEmpty(sessionId," ");
			userTranscriptions.setSessionId("1234");
			userTranscriptions.setTranscriptionReqId(Long.parseLong(transcriptionReqId));
			//TODO get supported format from formatting service and use
			userTranscriptions.setTranscribeResAvailableFormat("text/plain");
			userTranscriptions.setTranscribed(true);
			userTranscriptions.setDownloaded(false);
			Optional<AppUsers> appUsers = Optional.ofNullable(appUsersRepo.findByEmail(userEmail));
			if(appUsers.isPresent()){
				userTranscriptions.setUsername(appUsers.get().getUsername());
				userTranscriptions.setUserid(appUsers.get().getUserid());
				//TODO create TranscribeFileLog object to save or create TranscribeFileLog Trigger
			} else {
				throw new UserNotFoundException("Username not found for this Transcription request with Email: "+userEmail);
			}
			TranscribeFileLog transcribeFileLog = new TranscribeFileLog();
			transcribeFileLog.setFileName(file.getName());
			transcribeFileLog.setTReqId(Long.parseLong(transcriptionReqId));
			transcribeFileLog.setLogId(userTranscriptions.getLogId());
			transcribeFileLog.setEmail(userEmail);
			transcribeFileLog.setFileSize(size);
			transcribeFileLog.setSessionId(sessionId);
			transcribeFileLog.setTRes(transcribedText);
			userTranscriptions.setTranscribeResType("text/plain");
			userTranscriptions.setTranscribeFileLog(transcribeFileLog);
			userTranscriptions = userTranscriptionsRepository.save(userTranscriptions);
			Long tflogid = userTranscriptions.getTranscribeFileLog().getId();
			logger.debug("transcribefile  for requestId {} is saved  with tflogid ::{}", transcriptionReqId , tflogid);
		});
		
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		//return extractTranscribedTextFromSpeechRecognizer(recognizer, TranscriptionReqId);
		//TranscriptionResponseDto rDto = responseDto.
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
	
	
	/**
	 * Transcribes Audio with a transcribeReqId, and save the transcribed text to solr collection.
	 * audioFile format is required to be of wav format.
	 * @param audioFile
	 * @param reqId
	 * @return
	 * @throws IOException
	 * @throws ExecutionException
	 */
	public TranscriptionResponseDto transcribeAudio(File audioFile, @NotNull @NotBlank String reqId) throws  IOException, ExecutionException {
		
		SimpleAsyncTaskExecutor delegateExecutor =
				new SimpleAsyncTaskExecutor();
		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(sphinxConfiguration);
		InputStream stream = new FileInputStream(audioFile);
		TranscriptionResponseDto rdto = new TranscriptionResponseDto();
		
		recognizer.startRecognition(stream);
		
		//TODO call formatting method/service
		//return unformatted_transcribe_text;
		//wordsList = getWordsList(result.getWords());
		TranscriptionResponseDto responseDto = extractTranscribedTextFromSpeechRecognizer(recognizer, reqId);
		
		return  responseDto;
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
	public TranscriptionResponseDto transcribeVideo(File videoFile, String reqId) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

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
	 * @return TranscriptionResponseDto
	 */
	@Async
	private TranscriptionResponseDto extractTranscribedTextFromSpeechRecognizer(StreamSpeechRecognizer recognizer, String reqId) {
		
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
        
        return new TranscriptionResponseDto(unformattedTranscribeText.toString(), reqId, null, false, wordsList,null,null);
	}
}

