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
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FinancialUserRepository financialUserRepository;

    @AfterEach
    public void cleanUp() {
        financialUserRepository.deleteAll();
    }


    @Test
    public void testAuthenticationUser() {
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

        String accessToken = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0).split(";")[0].substring(10);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "authToken="+accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<AuthenticationResponse> validateLogin = restTemplate.exchange("/auth/check", HttpMethod.GET,entity, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK,validateLogin.getStatusCode());
    }

}
