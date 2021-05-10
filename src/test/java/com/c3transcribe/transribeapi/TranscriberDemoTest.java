/** */
package com.c3transcribe.transribeapi;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/** @author rajesh */
@Testable
public class TranscriberDemoTest {

  Clip clip;
  Configuration configuration = new Configuration();

  @BeforeEach
  public void setupConfiguration() {
    configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
    configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
    configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");
  }

  @Test
  public void testStreamSpeechRecognizer() throws Exception {

    StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
    InputStream stream = new FileInputStream(new File("/home/rajesh/Music/10001-90210-01803.wav"));

    recognizer.startRecognition(stream);
    SpeechResult result;
    while ((result = recognizer.getResult()) != null) {
      System.out.format("Hypothesis: %s\n", result.getHypothesis());
    }
    recognizer.stopRecognition();
  }

  @Test
  public void testStreamSpeechRecognizerWithStream() throws Exception {

    StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
    InputStream stream = new FileInputStream(new File("/home/rajesh/Music/10001-90210-01803.wav"));

    try {
      AudioInputStream astream = AudioSystem.getAudioInputStream(stream);
      this.clip = AudioSystem.getClip();
      this.clip.open(astream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    recognizer.startRecognition(stream);
    SpeechResult result;
    while ((result = recognizer.getResult()) != null) {
      System.out.format("Hypothesis: %s\n", result.getHypothesis());
    }
    recognizer.stopRecognition();
  }
}
