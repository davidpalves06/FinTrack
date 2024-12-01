package org.financk.financk_backend.budget.api;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetItemDTO;
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserBudgetResult> getUserBudget(@PathVariable("userId") UUID userId) {
        log.info("{} Request to get user budgets received", LOG_TITLE);
        ServiceResult<UserBudgetResult> serviceResult = budgetService.getUserBudgets(userId);
        log.info("{} Request to get user budgets successful with response {}", LOG_TITLE,serviceResult.getData());
        return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BudgetResult> createBudget(@RequestBody BudgetDTO budgetDTO) {
        log.info("{} Request to create budget received", LOG_TITLE);
        ServiceResult<BudgetResult> serviceResult = budgetService.createBudget(budgetDTO);
        log.info("{} Request to create budget successful", LOG_TITLE);
        return new ResponseEntity<>(serviceResult.getData(), HttpStatus.CREATED);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResult> getBudget(@PathVariable("budgetId") UUID budgetId) {
        log.info("{} Request to get budget with id {} received", LOG_TITLE,budgetId);
        ServiceResult<BudgetResult> serviceResult = budgetService.getBudget(budgetId);
        if (serviceResult.getErrorCode() == 1) {
            log.info("{} Request to get budget with id {} failed due to not being able to find budget", LOG_TITLE,budgetId);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        log.info("{} Request to get budget with id {} successful with response {}", LOG_TITLE,budgetId,serviceResult.getData());
        return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<String> updateBudget(@PathVariable("budgetId") UUID budgetId, @RequestBody BudgetDTO budgetDTO) {
        log.info("{} Request to update budget with id {} received", LOG_TITLE,budgetId);
        ServiceResult<String> serviceResult = budgetService.updateBudget(budgetId,budgetDTO);
        if (serviceResult.isSuccess()) {
            log.info("{} Request to update budget with id {} successful", LOG_TITLE,budgetId);
            return ResponseEntity.ok("");
        }
        else {
            log.info("{} Request to update budget with id {} failed", LOG_TITLE,budgetId);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable("budgetId") UUID budgetId) {
        log.info("{} Request to delete budget with id {} received", LOG_TITLE,budgetId);
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{budgetId}/expenses")
    public ResponseEntity<String> addBudgetItem(@PathVariable("budgetId") UUID budgetId,@RequestBody BudgetItemDTO budgetItemDTO) {
        log.info("{} Request to add budgetItem {} received", LOG_TITLE,budgetItemDTO);
        ServiceResult<BudgetResult> serviceResult = budgetService.addBudgetItemToBudget(budgetId, budgetItemDTO);
        if (serviceResult.isSuccess()) {
            log.info("{} Request to add budget with id {} successful", LOG_TITLE,budgetId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            log.info("{} Request to add budget with id {} failed", LOG_TITLE,budgetId);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{budgetId}/expenses/{itemId}")
    public ResponseEntity<String> deleteBudgetItem(@PathVariable("budgetId") UUID budgetId,@PathVariable("itemId") UUID itemId) {
        log.info("{} Request to delete budgetItem with id {} received", LOG_TITLE,itemId);
        ServiceResult<String> serviceResult = budgetService.deleteBudgetItem(budgetId, itemId);
        if (serviceResult.isSuccess()) {
            log.info("{} Request to delete budgetItem with id {} successful", LOG_TITLE,itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            log.info("{} Request to delete budgetItem with id {} failed", LOG_TITLE,itemId);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{budgetId}/expenses/{itemId}")
    public ResponseEntity<String> updateBudgetItem(@PathVariable("budgetId") UUID budgetId,@PathVariable("itemId") UUID itemId,
                                                   @RequestBody BudgetItemDTO budgetItemDTO) {
        log.info("{} Request to update budgetItem {} received", LOG_TITLE,budgetItemDTO);
        ServiceResult<String> serviceResult = budgetService.updateBudgetItem(budgetId,itemId, budgetItemDTO);
        if (serviceResult.isSuccess()) {
            log.info("{} Request to update budgetItem with id {} successful", LOG_TITLE,itemId);
            return ResponseEntity.ok("");
        }
        else {
            log.info("{} Request to update budgetItem with id {} failed", LOG_TITLE,itemId);
            return ResponseEntity.badRequest().build();
        }
    }

}
