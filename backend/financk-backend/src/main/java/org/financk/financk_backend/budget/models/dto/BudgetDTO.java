package org.financk.financk_backend.budget.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BudgetDTO {
    private UUID userID;
    private String budgetName;
    private float monthlyStartingBalance;

    public BudgetDTO(UUID userID, String budgetName) {
        this.userID = userID;
        this.budgetName = budgetName;
    }
}
