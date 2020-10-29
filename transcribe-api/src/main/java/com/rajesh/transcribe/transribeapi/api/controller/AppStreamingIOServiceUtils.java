package com.rajesh.transcribe.transribeapi.api.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class AppStreamingIOServiceUtils {
private static final Logger logger = getLogger(AppStreamingIOServiceUtils.class);
    
    final int chunkSize = 4 * 1024; // 4KB chunks
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
             logger.error("IOException occurred while reading from mp3Stream {}", e.getMessage());
         }
         return numBytesSaved;
     }
    
    
    /**
     *
     * @param multipartDataStream
     * @throws UnsupportedAudioFileException
     */
    public AudioFormat getMultipartStreamAudioFileFormat(MultipartFile multipartDataStream) throws UnsupportedAudioFileException{
        AudioFormat audioFormat = null;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(multipartDataStream.getInputStream());
            audioFormat = stream.getFormat();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException Occurred while fetching audioFormat from mp3Stream {}", e.getMessage());
        }
        return audioFormat;
    }
    
    
    
    /**
     *
     * @param fileInputStream
     * @throws UnsupportedAudioFileException
     */
    public AudioFormat getAudioFileFormatFromStream(FileInputStream fileInputStream) throws UnsupportedAudioFileException{
        AudioFormat audioFormat = null;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(fileInputStream);
            audioFormat = stream.getFormat();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException Occurred while fetching audioFormat from fileInputStream {}", e.getMessage());
        }
        return audioFormat;
    }
    
    
    /**
     *
     * @param fileInputStream
     * @throws UnsupportedAudioFileException
     */
    public AudioInputStream getAudioInputStreamFromFileInputStream(FileInputStream fileInputStream) throws UnsupportedAudioFileException{
        AudioInputStream stream = null;
        try {
            stream = AudioSystem.getAudioInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException Occurred while creating AudioInputStream from fileInputStream {}", e.getMessage());
        }
        return stream;
    }
     
}
