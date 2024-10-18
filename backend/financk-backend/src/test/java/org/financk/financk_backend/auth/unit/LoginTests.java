package org.financk.financk_backend.auth.unit;

import org.financk.financk_backend.auth.api.AuthenticationController;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.financk.financk_backend.auth.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class LoginTests {
    private final AuthenticationController authenticationController;
    private final AuthenticationService authenticationService;
    private final FinancialUserRepository financialUserRepository;

    public LoginTests() {
        financialUserRepository = Mockito.mock(FinancialUserRepository.class);
        authenticationService = new AuthenticationService(financialUserRepository,new JWTUtils());
        authenticationController = new AuthenticationController(authenticationService);
    }

    @AfterEach
    public void cleanUp() {
        financialUserRepository.deleteAll();
    }

    @Test
    public void testLoginFinancialUserSuccessfully() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");

        FinancialUser mockUser = new FinancialUser();
        mockUser.setEmail("test@test.com");
        mockUser.setPassword("$2a$10$ckQWdJFx7qJnhnYskCAApubnuX9JSq/uWWFKnnMbEp/BABv/b6JFK");

        when(financialUserRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        ResponseEntity<AuthenticationResponse> response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in.",response.getBody().getMessage());
    }

    @Test
    public void testLoginFinancialUserFailingDueToBadEmail() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test");
        authenticationDTO.setPassword("Password123");

        ResponseEntity<AuthenticationResponse> response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("test@.org");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setEmail("@test.org");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testLoginFinancialUserFailingDueToBadPassword() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("");

        ResponseEntity<AuthenticationResponse> response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("password");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("Password");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("PASSWORD123");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        authenticationDTO.setPassword("Pass1");

        response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testLoginFinancialUserFailingDueToInexistentUser() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password123");

        when(financialUserRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        ResponseEntity<AuthenticationResponse> response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testLoginFinancialUserFailingDueToWrongPassword() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail("test@test.com");
        authenticationDTO.setPassword("Password12");

        FinancialUser mockUser = new FinancialUser();
        mockUser.setEmail("test@test.com");
        //ENCODED PASSWORD123
        mockUser.setPassword("$2a$10$ckQWdJFx7qJnhnYskCAApubnuX9JSq/uWWFKnnMbEp/BABv/b6JFK");

        when(financialUserRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        ResponseEntity<AuthenticationResponse> response = authenticationController.loginUser(authenticationDTO);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
