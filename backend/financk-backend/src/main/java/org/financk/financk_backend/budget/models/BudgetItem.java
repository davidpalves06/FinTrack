package org.financk.financk_backend.budget.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class BudgetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID budgetId;
    private ItemType itemType;
    private String category;
    private float itemAmount;
    private LocalDate itemDate;
}
