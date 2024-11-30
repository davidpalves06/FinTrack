package org.financk.financk_backend.budget.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.financk.financk_backend.budget.models.BudgetItem;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BudgetDTO {
    private UUID budgetId;
    private String name;
    private List<BudgetItem> budgetItems;
    private Integer startingAmount;
    private Integer goalAmount;

    public BudgetDTO(UUID budgetId, String name) {
        this.budgetId = budgetId;
        this.name = name;
    }
}
