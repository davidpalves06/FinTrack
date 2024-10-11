package org.financk.financk_backend.auth;

import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FinancialUserRepository financialUserRepository;

    @AfterEach
    public void cleanUp() {
        financialUserRepository.deleteAll();
    }

    //TODO: LOGIN TEST CASES

    @Test
    public void testRegisterFinancialUser() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("password");
        authenticationDTO.setAge(20);
        authenticationDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity("/auth/register",authenticationDTO, AuthenticationResponse.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("User registered.",response.getBody().getMessage());
    }
}
