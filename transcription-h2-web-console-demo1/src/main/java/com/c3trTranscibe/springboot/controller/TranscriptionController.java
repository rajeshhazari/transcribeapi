package com.c3trTranscibe.springboot.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.c3trTranscibe.springboot.model.TranscriptionResponse;
import com.c3trTranscibe.springboot.services.TranscriptionService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
	
	
	
	 @RequestMapping( value= "/transcribeReqId" ,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public Map<String,Object> getTranscribeReqId(HttpServletRequest req, HttpServletResponse res) {
	    Map<String,Object> resp = new HashMap<String,Object>();
	    	resp.put("reqid", UUID.randomUUID().toString());
	    return resp;
	  }
	 
	 
    //Angular xample
    //http://jsfiddle.net/danialfarid/tqgr7pur/
    @PostMapping("/transcribe")
    public ResponseEntity<TranscriptionResponse> getTranscription(@RequestPart("meta-data") final String metaData,@RequestParam("fname")   @NotNull @NotBlank  final String fname,
            //@RequestParam("file") MultipartFile[] file) throws IOException {
    	    @RequestParam("file") final MultipartFile file) throws JsonParseException, JsonMappingException, IOException {  
    	logger.debug("Upload: {}", fname);
    	TranscriptionResponse response = null;
    	// MetaData document = objectMapper.readValue(metaData, MetaData.class);
    	if (Objects.isNull(file)){
    		return new ResponseEntity<TranscriptionResponse>(response, HttpStatus.BAD_REQUEST);
    	}
        if (!Objects.isNull(file) && !file.isEmpty()) {
        	response = tService.transcribeAudio(file.getResource().getFile());
            // store the bytes somewhere
            return new ResponseEntity<TranscriptionResponse>(response, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<TranscriptionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
