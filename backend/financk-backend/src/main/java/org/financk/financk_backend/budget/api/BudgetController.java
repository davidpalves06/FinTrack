package org.financk.financk_backend.budget.api;

import org.financk.financk_backend.budget.models.Budget;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @GetMapping
    public ResponseEntity<String> budget() {
        return ResponseEntity.ok("Budget");
    }

    @PostMapping
    public ResponseEntity<String> createBudget() {
        //TODO: CREATE BUDGET
        return ResponseEntity.ok("Budget");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getBudget(@PathVariable("userId") String userId) {
        //TODO: Get BUDGET
        return ResponseEntity.ok("Budget");
    }

    @PostMapping("/{budgetId}/expense")
    public ResponseEntity<String> addExpense(@PathVariable("budgetId") String budgetId) {
        // TODO: Add Expense to budget
        return ResponseEntity.ok("Budget");
    }

    @DeleteMapping("/{budgetId}/expense")
    public ResponseEntity<String> deleteBudget(@PathVariable("budgetId") String budgetId) {
        // TODO: Delete expense from budget
        return ResponseEntity.ok("Budget");
    }

    @PutMapping("/{budgetId}/expense")
    public ResponseEntity<String> updateBudget(@PathVariable("budgetId") String budgetId) {
        // TODO: Update expense from budget
        return ResponseEntity.ok("Budget");
    }
}
