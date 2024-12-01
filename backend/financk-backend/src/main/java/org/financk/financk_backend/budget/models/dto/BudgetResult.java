package org.financk.financk_backend.budget.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BudgetResult {
    private String message;
    private BudgetDTO budget;

    public BudgetResult(String message, BudgetDTO budget) {
        this.message = message;
        this.budget = budget;
    }
}
