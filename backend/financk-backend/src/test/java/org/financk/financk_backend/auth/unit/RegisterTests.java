package org.financk.financk_backend.auth.unit;

import org.financk.financk_backend.auth.api.AuthenticationController;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.financk.financk_backend.auth.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RegisterTests {
    private final AuthenticationController authenticationController;
    private final AuthenticationService authenticationService;
    private final FinancialUserRepository financialUserRepository;

    public RegisterTests() {
        financialUserRepository = Mockito.mock(FinancialUserRepository.class);
        authenticationService = new AuthenticationService(financialUserRepository,new JWTUtils());
        authenticationController = new AuthenticationController(authenticationService);
    }

    @AfterEach
    public void cleanUp() {
        financialUserRepository.deleteAll();
    }

    @Test
    public void testRegisterFinancialUserSuccessfully() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");
        authenticationDTO.setAge(20);
        authenticationDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered.",response.getBody().getMessage());
    }

    @Test
    public void testRegisterFinancialUserFailingDueToBadEmail() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test");
        authenticationDTO.setPassword("Password123");
        authenticationDTO.setAge(20);
        authenticationDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("test@.org");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("@test.org");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterFinancialUserFailingDueToBadPassword() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("");
        authenticationDTO.setAge(20);
        authenticationDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("password");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("Password");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("PASSWORD123");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterFinancialUserFailingDueToBadName() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");
        authenticationDTO.setAge(20);
        authenticationDTO.setName(" Test");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setName("Test ");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setName("Test");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setName("");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setName("Test Name Test");

        response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterFinancialUserFailingDueToBadAge() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");
        authenticationDTO.setAge(10);
        authenticationDTO.setName("Test Name");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegisterFinancialUserFailingDueToExistingEmail() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");
        authenticationDTO.setAge(20);
        authenticationDTO.setName("Test Name");

        when(financialUserRepository.existsByEmail("test@test.com")).thenReturn(true);

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerUser(authenticationDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already taken",response.getBody().getMessage());
    }
}
