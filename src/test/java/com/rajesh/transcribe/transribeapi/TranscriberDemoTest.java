/**
 * 
 */
package com.rajesh.transcribe.transribeapi;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author rajesh
 *
 */
public class TranscriberDemoTest {       

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");

	StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
	InputStream stream = new FileInputStream(new File("/home/rajesh/Music/10001-90210-01803.wav"));

        recognizer.startRecognition(stream);
	SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
	    System.out.format("Hypothesis: %s\n", result.getHypothesis());
	}
	recognizer.stopRecognition();
    }
}