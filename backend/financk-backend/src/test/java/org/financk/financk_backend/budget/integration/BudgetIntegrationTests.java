package org.financk.financk_backend.budget.integration;

import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.budget.models.BudgetItem;
import org.financk.financk_backend.budget.models.ItemType;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetItemDTO;
import org.financk.financk_backend.budget.models.dto.BudgetResult;
import org.financk.financk_backend.budget.models.dto.UserBudgetResult;
import org.financk.financk_backend.budget.repository.BudgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
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
        UUID userId = UUID.randomUUID();

        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setUserId(userId);
        budgetDTO.setName("Test Budget");
        budgetDTO.setStartingAmount(1000F);
        budgetDTO.setGoalAmount(10000F);

        HttpEntity<BudgetDTO> creationRequest = new HttpEntity<>(budgetDTO, headers);
        ResponseEntity<BudgetResult> creationResponse = restTemplate.exchange("/budgets",HttpMethod.POST,creationRequest, BudgetResult.class);
        assertEquals(HttpStatus.CREATED,creationResponse.getStatusCode());

        UUID budgetID = creationResponse.getBody().getBudget().getBudgetId();

        ResponseEntity<BudgetResult> getBucketResponse = getBudgetRequest(headers, budgetID);
        BudgetDTO budget = getBucketResponse.getBody().getBudget();
        assertEquals(budgetDTO.getStartingAmount(),budget.getStartingAmount());
        assertEquals(budgetDTO.getName(),budget.getName());
        assertEquals(budgetDTO.getGoalAmount(),budget.getGoalAmount());

        HttpEntity<BudgetDTO> userBudgetsRequest = new HttpEntity<>(headers);
        ResponseEntity<UserBudgetResult> userBudgetsResponse = restTemplate.exchange("/budgets/users/" + userId,HttpMethod.GET,userBudgetsRequest, UserBudgetResult.class);
        assertEquals(HttpStatus.OK,userBudgetsResponse.getStatusCode());
        assertEquals(budgetID,userBudgetsResponse.getBody().getBudgets().get(0).getBudgetId());

        BudgetItemDTO budgetItemDTO = new BudgetItemDTO();
        budgetItemDTO.setCategory("Test Item");
        budgetItemDTO.setItemType(ItemType.EXPENSE);
        budgetItemDTO.setItemDate(LocalDate.now());
        budgetItemDTO.setItemAmount(100F);

        HttpEntity<BudgetItemDTO> addItemRequest = new HttpEntity<>(budgetItemDTO, headers);
        ResponseEntity<String> addItemResponse = restTemplate.exchange("/budgets/" + budgetID + "/expenses",HttpMethod.POST,addItemRequest, String.class);
        assertEquals(HttpStatus.CREATED,addItemResponse.getStatusCode());

        getBucketResponse = getBudgetRequest(headers, budgetID);
        assertEquals(HttpStatus.OK,getBucketResponse.getStatusCode());
        budget = getBucketResponse.getBody().getBudget();
        assertEquals(900F,budget.getCurrentAmount());

        UUID budgetItemID = budget.getBudgetItems().get(0).getId();
        budgetItemDTO.setItemAmount(200F);
        HttpEntity<BudgetItemDTO> updateItemRequest = new HttpEntity<>(budgetItemDTO, headers);
        ResponseEntity<String> updateItemResponse = restTemplate.exchange("/budgets/" + budgetID + "/expenses/" + budgetItemID,HttpMethod.PUT,updateItemRequest, String.class);
        assertEquals(HttpStatus.OK,updateItemResponse.getStatusCode());

        getBucketResponse = getBudgetRequest(headers, budgetID);
        assertEquals(HttpStatus.OK,getBucketResponse.getStatusCode());
        budget = getBucketResponse.getBody().getBudget();
        assertEquals(800F,budget.getCurrentAmount());

        budgetItemDTO.setItemAmount(200F);
        budgetItemDTO.setItemType(ItemType.INCOME);
        addItemRequest = new HttpEntity<>(budgetItemDTO, headers);
        addItemResponse = restTemplate.exchange("/budgets/" + budgetID + "/expenses",HttpMethod.POST,addItemRequest, String.class);
        assertEquals(HttpStatus.CREATED,addItemResponse.getStatusCode());

        getBucketResponse = getBudgetRequest(headers, budgetID);
        assertEquals(HttpStatus.OK,getBucketResponse.getStatusCode());
        budget = getBucketResponse.getBody().getBudget();
        assertEquals(1000F,budget.getCurrentAmount());

        HttpEntity<Void> deleteItemRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteItemResponse = restTemplate.exchange("/budgets/" + budgetID + "/expenses/" + budgetItemID,HttpMethod.DELETE,deleteItemRequest, String.class);
        assertEquals(HttpStatus.NO_CONTENT,deleteItemResponse.getStatusCode());

        getBucketResponse = getBudgetRequest(headers, budgetID);
        assertEquals(HttpStatus.OK,getBucketResponse.getStatusCode());
        budget = getBucketResponse.getBody().getBudget();
        assertEquals(1200F,budget.getCurrentAmount());
        assertEquals(1,budget.getBudgetItems().size());

        HttpEntity<Void> deleteBudgetRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteBudgetResponse = restTemplate.exchange("/budgets/" + budgetID,HttpMethod.DELETE,deleteBudgetRequest, String.class);
        assertEquals(HttpStatus.NO_CONTENT,deleteBudgetResponse.getStatusCode());

        getBucketResponse = getBudgetRequest(headers, budgetID);
        assertEquals(HttpStatus.NOT_FOUND,getBucketResponse.getStatusCode());
    }

    private ResponseEntity<BudgetResult> getBudgetRequest(HttpHeaders headers, UUID budgetID) {
        HttpEntity<Void> getBudgetRequest = new HttpEntity<>(headers);
        return restTemplate.exchange("/budgets/" + budgetID,HttpMethod.GET,getBudgetRequest, BudgetResult.class);
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
