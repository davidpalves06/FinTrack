package org.financk.financk_backend.budget.repository;

import org.financk.financk_backend.budget.models.Budget;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends CrudRepository<Budget, UUID> {

    List<Budget> findBudgetsByUserId(UUID userId);
}
