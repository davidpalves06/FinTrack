package org.financk.financk_backend.auth.integration;

import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationIntegrationTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
        registerDTO.setUsername("TestUser");
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

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> validateLogin = restTemplate.exchange("/budgets", HttpMethod.GET,entity, String.class);
        assertEquals(HttpStatus.OK,validateLogin.getStatusCode());
    }

    @Test
    public void testRefreshTokenFinancialUser() {
        AuthenticationDTO registerDTO = new AuthenticationDTO();
        registerDTO.setEmail("test@test.com");
        registerDTO.setPassword("Password123");
        registerDTO.setUsername("TestUser");
        registerDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> registerResponse = restTemplate.postForEntity("/auth/register",registerDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.CREATED,registerResponse.getStatusCode());
        assertEquals("User registered.",registerResponse.getBody().getMessage());

        AuthenticationDTO loginDTO = new AuthenticationDTO();
        loginDTO.setEmail("test@test.com");
        loginDTO.setPassword("Password123");
        ResponseEntity<AuthenticationResponse> loginResponse = restTemplate.postForEntity("/auth/login",loginDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK,loginResponse.getStatusCode());

        String refreshToken = loginResponse.getBody().getRefreshToken();

        AuthenticationDTO refreshDTO = new AuthenticationDTO();
        refreshDTO.setRefreshToken(refreshToken);
        ResponseEntity<AuthenticationResponse> refreshResponse = restTemplate.postForEntity("/auth/refresh",refreshDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK,refreshResponse.getStatusCode());

        String accessToken = refreshResponse.getBody().getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> validateLogin = restTemplate.exchange("/budgets", HttpMethod.GET,entity, String.class);
        assertEquals(HttpStatus.OK,validateLogin.getStatusCode());
    }
}
