package org.financk.financk_backend.budget.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    @OneToMany(mappedBy = "budgetId",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetItem> budgetItems;
    private String name;
    private int startingAmount;
    private int goalAmount;
}
