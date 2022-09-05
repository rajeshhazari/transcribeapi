package com.rajesh.transcribe.controllers;

import com.c3transcribe.transcribeapi.TranscribeApiApplication;
import com.c3transcribe.transcribeapi.api.models.dto.AuthenticationRequestDto;
import com.c3transcribe.transcribeapi.api.models.dto.AuthenticationResponseDto;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig @SpringJUnitWebConfig
@AutoConfigureMockMvc
@EnabledIf(
        expression = "#{systemProperties['os.name'].toLowerCase().contains('ubuntu')}",
        reason = "Enabled on Mac OS"
)
@ContextConfiguration(classes = {TranscribeApiApplication.class})
@WebMvcTest
public class JwtAuthControllerTest {
    private final static String TEST_USER_EMAIL = "rajesh_hazari@yahoo.com";
    private final static String TEST_USER_PASSWORD = "admin123";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuth() throws Exception {
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
