package org.financk.financk_backend.budget.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Expense {
    private ExpenseType expenseType;
    private float expenseAmount;
    private Date expenseDate;
}
