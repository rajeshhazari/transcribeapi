package com.rajesh.transcribe.transribeapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@TestConfiguration
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
public class AppRestServiceInitTestConfig {
    
    @Bean
    public MockRestServiceServer mockRestServiceServer() {
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        
        server.expect(MockRestRequestMatchers.requestTo("http://www.localhost:9090"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
        
        return server;
    }
    
    @Autowired
    private RestTemplate restTemplate;
}
