package com.c3trTranscibe.springboot.controller;

import java.io.IOException;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.c3trTranscibe.springboot.model.TranscriptionResponse;
import com.c3trTranscibe.springboot.services.TranscriptionService;

/**
 *
 */
@RestController
public class TranscriptionController {
	
	
    private Logger logger = LoggerFactory.getLogger(TranscriptionController.class);

    
	@Autowired
	Environment env;
	
	@Autowired
	TranscriptionService tService;
	
    
    //Angular xample
    //http://jsfiddle.net/danialfarid/tqgr7pur/
    @PostMapping("/transcribe")
    public ResponseEntity<?> handleFormUpload(@RequestPart("meta-data") final String metaData,@RequestParam("fname")   @NotNull @NotBlank  final String fname,
            //@RequestParam("file") MultipartFile[] file) throws IOException {
    	    @RequestParam("file") final MultipartFile file) throws JsonParseException, JsonMappingException, IOException {  
    	logger.debug("Upload: {}", fname);
    	
    	// MetaData document = objectMapper.readValue(metaData, MetaData.class);
    	if (Objects.isNull(file)){
    		return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
    	}
        if (!Objects.isNull(file) && !file.isEmpty()) {
        	TranscriptionResponse response = tService.transcribeAudio(file.getResource().getFile());
            // store the bytes somewhere
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    

}



/**
 * @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    Advertisement storeAd(@RequestPart("ad") String adString, @RequestPart("file") MultipartFile file) throws IOException {

        Advertisement jsonAd = new ObjectMapper().readValue(adString, Advertisement.class);
//do whatever you want with your file and jsonAd
 * 
 */
