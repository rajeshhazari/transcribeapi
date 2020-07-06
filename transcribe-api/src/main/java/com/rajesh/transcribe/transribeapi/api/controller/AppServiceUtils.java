package com.rajesh.transcribe.transribeapi.api.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class AppServiceUtils {
private static final Logger logger = getLogger(AppServiceUtils.class);
    
    /**
     * @param file
     * @param uploadDir
     * @return
     * @throws IOException
     */
    public File convertMultipartFileToFile(MultipartFile file, final String uploadDir) throws IOException {
        file.transferTo(Paths.get(uploadDir+file.getOriginalFilename()));
        File convFile = new File(uploadDir+file.getOriginalFilename());
        logger.info("Audio file uploaded to: {}  content type :: {}  with size {} ", uploadDir, file.getContentType(), file.getSize());
        /*try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }*/
        return convFile;
    }
    
    /**
     *
     * @param mp3DataStream
     * @param uploadDir
     * @throws UnsupportedAudioFileException
     */
     public int mp3ToWav(MultipartFile mp3DataStream, final  String uploadDir) throws UnsupportedAudioFileException{
         int numBytesSaved = 0;
         try {
             mp3DataStream.transferTo(Paths.get(uploadDir+mp3DataStream.getOriginalFilename()));
             File file = new File(uploadDir+mp3DataStream.getOriginalFilename());
             AudioInputStream mp3InputStream = AudioSystem.getAudioInputStream(mp3DataStream.getInputStream());
             AudioFormat audioFormat = mp3InputStream.getFormat();
             numBytesSaved = AudioSystem.write(mp3InputStream, AudioFileFormat.Type.WAVE, file);
         } catch (IOException e) {
             e.printStackTrace();
             numBytesSaved = -1;
             logger.error("IOException Occurred while reading from mp3Stream {}", e.getMessage());
         }
         return numBytesSaved;
     }
     
    /**
     *
     * @param size
     * @param algorith
     * @return random
     */
    public String random(int size, String algorith) {
        
        StringBuilder generatedToken = new StringBuilder();
        String defaultAlg = "SHA1PRNG";
        if(StringUtils.hasText(algorith)){
            defaultAlg = algorith;
        }
        try {
            String number = SecureRandom.getInstance(defaultAlg).generateSeed(size).toString();
            generatedToken.append(RandomStringUtils.random(size, 0, 0, true, true, null, new SecureRandom()));
        } catch (NoSuchAlgorithmException e) {
            logger.error("unable to generate random password ", e.getMessage());
        }
        return generatedToken.toString();
    }
}
