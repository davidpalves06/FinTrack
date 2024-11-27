package org.financk.financk_backend.budget.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Budget {
    private UUID id;
    private UUID userID;
    private Map<String,YearlyBudget> yearlyBudgets;
    private float monthlyStartingBalance;

}
