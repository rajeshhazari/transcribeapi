package com.rajesh.transcribe.controllers;

import com.c3transcribe.transcribeapi.TranscribeApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.c3transcribe.transcribeapi.api.models.dto.*;
import com.c3transcribe.transcribeapi.api.controller.JwtAuthenticationController;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ContextConfiguration(classes = {TranscribeApiApplication.class,JwtAuthenticationController.class})
@WebMvcTest
public class JwtAuthControllerTest {
    private final static String TEST_USER_EMAIL = "rajesh_hazari@yahoo.com";
    private final static String TEST_USER_PASSWORD = "admin123";

    @Autowired
    private MockMvc mockMvc;

    @Test
    private void testAuth() throws Exception {
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
        authenticationRequestDto.setUsername(TEST_USER_EMAIL);
        authenticationRequestDto.setPassword(TEST_USER_PASSWORD);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth",authenticationRequestDto)
                //.with(password(TEST_USER_PASSWORD))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        AuthenticationResponseDto resultAuth = (AuthenticationResponseDto) result.getAsyncResult();
        assertNotNull(resultAuth);
    }
}
