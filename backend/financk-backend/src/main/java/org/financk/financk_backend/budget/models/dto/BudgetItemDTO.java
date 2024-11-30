package org.financk.financk_backend.budget.models.dto;

import lombok.Data;
import org.financk.financk_backend.budget.models.ItemType;

import java.time.LocalDate;

@Data
public class BudgetItemDTO {
    private ItemType itemType;
    private Float itemAmount;
    private LocalDate itemDate;
}
