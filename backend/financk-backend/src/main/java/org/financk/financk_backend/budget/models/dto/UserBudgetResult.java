package org.financk.financk_backend.budget.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserBudgetResult {
    private String message;
    private List<BudgetDTO> budgets;

    public UserBudgetResult(String message, List<BudgetDTO> budgets) {
        this.message = message;
        this.budgets = budgets;
    }
}
