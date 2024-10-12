package org.financk.financk_backend.auth.integration;

import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FinancialUserRepository financialUserRepository;

    @AfterEach
    public void cleanUp() {
        financialUserRepository.deleteAll();
    }


    @Test
    public void testRegisterFinancialUser() {
        AuthenticationDTO registerDTO = new AuthenticationDTO();
        registerDTO.setEmail("test@test.com");
        registerDTO.setPassword("Password123");
        registerDTO.setAge(20);
        registerDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> registerResponse = restTemplate.postForEntity("/auth/register",registerDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.CREATED,registerResponse.getStatusCode());
        assertEquals("User registered.",registerResponse.getBody().getMessage());

        AuthenticationDTO loginDTO = new AuthenticationDTO();
        loginDTO.setEmail("test@test.com");
        loginDTO.setPassword("Password123");
        ResponseEntity<AuthenticationResponse> loginResponse = restTemplate.postForEntity("/auth/login",loginDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK,loginResponse.getStatusCode());

        String accessToken = loginResponse.getBody().getAccessToken();

        //TODO: SWITCH THIS WITH ANOTHER ENDPOINT THAT NEEDS AUTHENTICATION
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<AuthenticationDTO> entity = new HttpEntity<>(new AuthenticationDTO(), headers);
        ResponseEntity<AuthenticationResponse> validateLogin = restTemplate.postForEntity("/auth/recover-password",entity, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK,validateLogin.getStatusCode());
    }
}
