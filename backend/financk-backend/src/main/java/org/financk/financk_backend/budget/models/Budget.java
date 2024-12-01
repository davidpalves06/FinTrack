package org.financk.financk_backend.budget.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    @OneToMany(mappedBy = "budgetId",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BudgetItem> budgetItems = new ArrayList<>();
    private String name;
    private float startingAmount;
    private float currentAmount;
    private float goalAmount;
}
