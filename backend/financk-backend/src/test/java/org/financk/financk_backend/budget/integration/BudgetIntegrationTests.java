package org.financk.financk_backend.budget.integration;

import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetResult;
import org.financk.financk_backend.budget.repository.BudgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BudgetIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private FinancialUserRepository financialUserRepository;

    @AfterEach
    public void cleanUp() {
        budgetRepository.deleteAll();
        financialUserRepository.deleteAll();
    }

    @Test
    public void testBudgets() {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "authToken="+accessToken);
        headers.set("Content-Type", "application/json");

        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setUserId(UUID.randomUUID());
        budgetDTO.setName("Test Budget");
        budgetDTO.setStartingAmount(1000F);
        budgetDTO.setGoalAmount(10000F);

        HttpEntity<BudgetDTO> creationRequest = new HttpEntity<>(budgetDTO, headers);
        ResponseEntity<BudgetResult> creationResponse = restTemplate.exchange("/budgets",HttpMethod.POST,creationRequest, BudgetResult.class);
        assertEquals(HttpStatus.CREATED,creationResponse.getStatusCode());

        HttpEntity<Void> getBudgetRequest = new HttpEntity<>(headers);
        ResponseEntity<BudgetResult> getBucketResponse = restTemplate.exchange("/budgets/" + creationResponse.getBody().getBudget().getBudgetId(),HttpMethod.GET,getBudgetRequest, BudgetResult.class);
        assertEquals(HttpStatus.OK,getBucketResponse.getStatusCode());
        BudgetDTO budget = getBucketResponse.getBody().getBudget();
        assertEquals(budgetDTO.getStartingAmount(),budget.getStartingAmount());
        assertEquals(budgetDTO.getName(),budget.getName());
        assertEquals(budgetDTO.getGoalAmount(),budget.getGoalAmount());

    }

    private String getAccessToken() {
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

        return loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0).split(";")[0].substring(10);
    }
}
