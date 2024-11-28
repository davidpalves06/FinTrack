package org.financk.financk_backend.budget.api;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetResult;
import org.financk.financk_backend.budget.models.dto.UserBudgetResult;
import org.financk.financk_backend.budget.service.BudgetService;
import org.financk.financk_backend.common.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/budgets")
public class BudgetController {
    private static final String LOG_TITLE = "[BudgetController] -";
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<String> budget() {
        return ResponseEntity.ok("Budget");
    }

    @PostMapping
    public ResponseEntity<BudgetResult> createBudget(@RequestBody BudgetDTO budgetDTO) {
        if (budgetDTO.getMonthlyStartingBalance() < 0) {
            return ResponseEntity.badRequest().build();
        }
        ServiceResult<BudgetResult> budgetServiceResult = budgetService.createBudget(budgetDTO);
        return new ResponseEntity<>(budgetServiceResult.getData(), HttpStatus.CREATED);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResult> getBudget(@PathVariable("budgetId") UUID budgetId) {
        ServiceResult<BudgetResult> serviceResult = budgetService.getBudget(budgetId);
        if (serviceResult.getErrorCode() == 1) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<BudgetResult> deleteBudget(@PathVariable("budgetId") UUID budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{budgetId}/expense")
    public ResponseEntity<String> addExpense(@PathVariable("budgetId") String budgetId) {
        // TODO: Add Expense to budget
        return ResponseEntity.ok("Budget");
    }

    @DeleteMapping("/{budgetId}/expense")
    public ResponseEntity<String> deleteExpense(@PathVariable("budgetId") String budgetId) {
        // TODO: Delete expense from budget
        return ResponseEntity.ok("Budget");
    }

    @PutMapping("/{budgetId}/expense")
    public ResponseEntity<String> updateBudget(@PathVariable("budgetId") String budgetId) {
        // TODO: Update expense from budget
        return ResponseEntity.ok("Budget");
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserBudgetResult> getUserBudgets(@PathVariable("userId") UUID userId) {
        ServiceResult<UserBudgetResult> serviceResult = budgetService.getUserBudgets(userId);
        return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
    }
}
