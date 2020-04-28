package com.rajesh.transcribe.transribeapi.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(classes = AppRestServiceInitTestConfig.class)
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
@WebMvcTest(AppInfoController.class)
@ActiveProfiles("test")
public class AppInfoControllerTest {
    
    @Autowired
    WebApplicationContext wac;
    
    @Test
    public void testTypes() throws Exception {
        webAppContextSetup(wac).build().perform(get("/api/v1/version").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
                                .andExpect((ResultMatcher) jsonPath("$.info.app.version", "0.0.1-SNAPSHOT"));
    }
    
}


