package org.financk.financk_backend.budget.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class YearlyBudget {
    private UUID id;
    private UUID budgetId;
    private Map<String,MonthlyBudget> monthlyBudgets;
}
