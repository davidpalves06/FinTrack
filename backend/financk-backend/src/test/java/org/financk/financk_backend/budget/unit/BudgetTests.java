package org.financk.financk_backend.budget.unit;

import org.financk.financk_backend.auth.api.AuthenticationController;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.financk.financk_backend.auth.service.AuthenticationService;
import org.financk.financk_backend.budget.api.BudgetController;
import org.financk.financk_backend.budget.models.Budget;
import org.financk.financk_backend.budget.models.BudgetItem;
import org.financk.financk_backend.budget.models.ItemType;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetItemDTO;
import org.financk.financk_backend.budget.models.dto.BudgetResult;
import org.financk.financk_backend.budget.models.dto.UserBudgetResult;
import org.financk.financk_backend.budget.repository.BudgetRepository;
import org.financk.financk_backend.budget.service.BudgetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BudgetTests {
    private final BudgetController budgetController;
    private final BudgetService budgetService;
    private final BudgetRepository budgetRepository;

    public BudgetTests() {
        budgetRepository = Mockito.mock(BudgetRepository.class);
        budgetService = new BudgetService(budgetRepository);
        budgetController = new BudgetController(budgetService);
    }

    @AfterEach
    public void cleanUp() {
        budgetRepository.deleteAll();
    }

    @Test
    public void testCreateBudgetSuccessfully() {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setUserId(UUID.randomUUID());
        budgetDTO.setName("Test Budget");
        budgetDTO.setStartingAmount(1000F);
        budgetDTO.setGoalAmount(10000F);

        when(budgetRepository.save(any())).thenAnswer((invocation) -> invocation.getArgument(0));
        ResponseEntity<BudgetResult> response = budgetController.createBudget(budgetDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Budget created",response.getBody().getMessage());
    }

    @Test
    public void testGetUserBudgetsSuccessfully() {
        UUID userId = UUID.randomUUID();
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(userId);
        budget.setName("Test Budget");

        when(budgetRepository.findBudgetsByUserId(userId)).thenReturn(List.of(budget));
        ResponseEntity<UserBudgetResult> response = budgetController.getUserBudget(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget.getId(),response.getBody().getBudgets().get(0).getBudgetId());
        assertEquals("Test Budget",response.getBody().getBudgets().get(0).getName());
    }

    @Test
    public void testGetBudgetSuccessfully() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(UUID.randomUUID());
        budgetItem.setItemType(ItemType.EXPENSE);
        budgetItem.setItemDate(LocalDate.now());
        budgetItem.setCategory("Test Category");
        budgetItem.setItemAmount(10f);

        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setGoalAmount(10000F);
        budget.setBudgetItems(List.of(budgetItem));

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));
        ResponseEntity<BudgetResult> response = budgetController.getBudget(budget.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget.getId(),response.getBody().getBudget().getBudgetId());
        assertEquals("Test Budget",response.getBody().getBudget().getName());
        assertEquals(1,response.getBody().getBudget().getBudgetItems().size());
        assertEquals(1000F,response.getBody().getBudget().getStartingAmount());
    }

    @Test
    public void testGetBudgetFailed() {
        when(budgetRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<BudgetResult> response = budgetController.getBudget(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddBudgetItemSuccessfully() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setCurrentAmount(1000F);
        budget.setGoalAmount(10000F);

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

        BudgetItemDTO budgetItemDTO = new BudgetItemDTO();
        budgetItemDTO.setCategory("Test Category");
        budgetItemDTO.setItemAmount(500F);
        budgetItemDTO.setItemDate(LocalDate.now());
        budgetItemDTO.setItemType(ItemType.EXPENSE);
        ResponseEntity<String> response = budgetController.addBudgetItem(budget.getId(), budgetItemDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testAddBudgetItemFailed() {
        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setCurrentAmount(1000F);
        budget.setGoalAmount(10000F);

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.empty());

        BudgetItemDTO budgetItemDTO = new BudgetItemDTO();
        budgetItemDTO.setCategory("Test Category");
        budgetItemDTO.setItemAmount(500F);
        budgetItemDTO.setItemDate(LocalDate.now());
        budgetItemDTO.setItemType(ItemType.EXPENSE);
        ResponseEntity<String> response = budgetController.addBudgetItem(budget.getId(), budgetItemDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteBudgetItemSuccessfully() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(UUID.randomUUID());
        budgetItem.setItemType(ItemType.EXPENSE);
        budgetItem.setItemDate(LocalDate.now());
        budgetItem.setCategory("Test Category");
        budgetItem.setItemAmount(10f);

        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setCurrentAmount(1000F);
        budget.setGoalAmount(10000F);
        budget.setBudgetItems(new ArrayList<>(List.of(budgetItem)));

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

        ResponseEntity<String> response = budgetController.deleteBudgetItem(budget.getId(), budgetItem.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0,budget.getBudgetItems().size());
    }

    @Test
    public void testUpdateBudgetItemSuccessfully() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(UUID.randomUUID());
        budgetItem.setItemType(ItemType.EXPENSE);
        budgetItem.setItemDate(LocalDate.now());
        budgetItem.setCategory("Test Category");
        budgetItem.setItemAmount(100f);

        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setCurrentAmount(900F);
        budget.setGoalAmount(10000F);
        budget.setBudgetItems(new ArrayList<>(List.of(budgetItem)));

        BudgetItemDTO budgetItemDTO = new BudgetItemDTO();
        budgetItemDTO.setCategory("Test Category");
        budgetItemDTO.setItemAmount(500F);
        budgetItemDTO.setItemDate(LocalDate.now());
        budgetItemDTO.setItemType(ItemType.EXPENSE);

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));

        ResponseEntity<String> response = budgetController.updateBudgetItem(budget.getId(), budgetItem.getId(),budgetItemDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(500f,budget.getCurrentAmount());
    }

    @Test
    public void testUpdateBudgetSuccessfully(){
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setUserId(UUID.randomUUID());
        budgetDTO.setName("Test Budget");
        budgetDTO.setStartingAmount(1000F);
        budgetDTO.setGoalAmount(30000F);

        Budget budget = new Budget();
        budget.setId(UUID.randomUUID());
        budget.setUserId(UUID.randomUUID());
        budget.setName("Test Budget");
        budget.setStartingAmount(1000F);
        budget.setCurrentAmount(900F);
        budget.setGoalAmount(10000F);

        when(budgetRepository.findById(budget.getId())).thenReturn(Optional.of(budget));
        ResponseEntity<String> response = budgetController.updateBudget(budget.getId(),budgetDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetDTO.getGoalAmount(),budget.getGoalAmount());
    }


}
