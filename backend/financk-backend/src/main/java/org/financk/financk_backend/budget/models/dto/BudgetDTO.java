package org.financk.financk_backend.budget.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.financk.financk_backend.budget.models.BudgetItem;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BudgetDTO {
    private UUID budgetId;
    private UUID userId;
    private String name;
    private List<BudgetItem> budgetItems;
    private Float startingAmount;
    private Float currentAmount;
    private Float goalAmount;

    public BudgetDTO(UUID budgetId, String name) {
        this.budgetId = budgetId;
        this.name = name;
    }
}
