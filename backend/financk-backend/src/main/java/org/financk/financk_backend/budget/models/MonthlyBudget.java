package org.financk.financk_backend.budget.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MonthlyBudget {
    private UUID id;
    private UUID yearlyBudgetId;
    private float startingAmount;
    private float remainingAmount;
    private List<Expense> expenses;
}
