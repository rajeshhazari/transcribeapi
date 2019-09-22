package com.example.test;

import java.net.URI;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.c3trTranscibe.springboot.TranscribtionH2WebDemoApp;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TranscribtionH2WebDemoApp.class)
@AutoConfigureMockMvc
@ActiveProfiles("default")
public class GlobalExceptionHandlerIntegrationTest {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerIntegrationTest.class);

	
	public static final String ISO8601_DATE_REGEX =
	        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

	    
	    @Autowired
	     private TestRestTemplate restTemplate;

	     @LocalServerPort
	     private int port;

	     private String getRootUrl() {
	         return "http://localhost:" + port;
	     }

	/*
	 * @Test public void contextLoads() {
	 * 
	 * }
	 */
	    
	    @Test
	    public void invalidUrl_returnsHttp404() throws Exception {
	    	 HttpHeaders headers = new HttpHeaders();
	    	//setting up the HTTP Basic Authentication header value
	         String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary(("rajesh" + ":" + "admin#@!79").getBytes());
	         headers.add("Authorization", authorizationHeader);
	         headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
	        RequestBuilder requestBuilder = getGetRequestBuilder("/does-not-exist");
	        RequestEntity<String> requestEntity = new RequestEntity<String>(HttpMethod.GET, new URI(getRootUrl()+"/does-not-exist"));
			ResponseEntity<String> responseType  = restTemplate.exchange(getRootUrl()+"/does-not-exist", HttpMethod.GET, requestEntity, String.class);
			if(responseType.getStatusCode() == HttpStatus.NOT_FOUND) {
				
			}
	           /* .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.code", is(1000)))
	            .andExpect(jsonPath("$.message", is("No handler found for your request.")))
	            .andExpect(jsonPath("$.timestamp", RegexMatcher.matchesRegex(ISO8601_DATE_REGEX)));*/
	    }

	    private RequestBuilder getGetRequestBuilder(String url) {
	        return MockMvcRequestBuilders
	            .get(url)
	            .accept(MediaType.APPLICATION_JSON);
	    }
	    
}
