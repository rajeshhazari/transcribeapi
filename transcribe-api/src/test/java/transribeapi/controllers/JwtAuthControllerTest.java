package transribeapi.controllers;

import com.rajesh.transcribe.transribeapi.TranscribeapiApplication;
import com.rajesh.transcribe.transribeapi.api.controller.JwtAuthenticationController;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationRequestDto;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthenticationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ContextConfiguration(classes = {TranscribeapiApplication.class,JwtAuthenticationController.class})
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
