/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rajesh.transcribe.utils;

import java.io.*;
import java.io.InputStream;

import com.c3transcribe.core.utils.ReusableBufferedInputStream;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatObject;

/**
 *
 * @author rajesh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
public class ReusableBufferedInputStreamTest {

    Configuration configuration = new Configuration();
    StreamSpeechRecognizer recognizer = null;
    public static final String sampleWaveFilePath = "/home/rajesh/Music/10001-90210-01803.wav" ;

    @BeforeAll
    public void setUpConfiguration() throws IOException {


        configuration.setAcousticModelPath("resource:/transcribe/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/transcribe/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/transcribe/models/en-us/en-us.lm.bin");
        recognizer = new StreamSpeechRecognizer(configuration);
    }
    @Test
    public void testTranscribeResponseFromFile() throws Exception {

        InputStream stream = new FileInputStream(new File(sampleWaveFilePath));

        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
    }

    @Test
    public void reusableInputStreamTest() throws IOException{
        
        InputStream is = new ByteArrayInputStream("sample".getBytes());
        InputStream stream = new FileInputStream(new File(sampleWaveFilePath));
        ReusableBufferedInputStream decoratedIs = new ReusableBufferedInputStream(stream);
        assertThatObject(decoratedIs).isNotNull();
        assertThat(decoratedIs.readAllBytes().length).isEqualTo("sample".getBytes().length);


        recognizer.startRecognition(decoratedIs);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
    }

    @Test
    public void reusableInputStreamTestStreamOfBytesReadFromFis() throws IOException{

    }
}

