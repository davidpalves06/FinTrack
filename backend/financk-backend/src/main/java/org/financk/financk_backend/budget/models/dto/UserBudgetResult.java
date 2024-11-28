package org.financk.financk_backend.budget.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserBudgetResult {
    private String message;
    private List<BudgetDTO> budgets;

    public UserBudgetResult(String message, List<BudgetDTO> budgets) {
        this.message = message;
        this.budgets = budgets;
    }
}
