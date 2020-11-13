/** */
package com.rajesh.transcribe;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import javax.sound.sampled.*;

import edu.cmu.sphinx.tools.audio.AudioDataInputStream;
import org.apache.commons.configuration2.io.InputStreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/** @author rajesh */

public class TranscriberDemoTest_1 {

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
  
    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    System.out.println(simpleAsyncTaskExecutor.isThrottleActive());
    InputStream stream = new FileInputStream(new File("/home/rajesh/Music/10001-90210-01803.wav"));
    StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
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
    byte[] data = new byte[4096];
    
    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
    float rate = 44100.0f;
    int channels = 2;
    int sampleSize = 16;
    boolean bigEndian = true;
    InetAddress addr;
  
    AudioFormat pcmSignedAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
  
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, pcmSignedAudioFormat);
    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("Line matching " + info + " not supported.");
      return;
    }
    
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
