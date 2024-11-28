package org.financk.financk_backend.budget.repository;

import org.financk.financk_backend.budget.models.Budget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, UUID> {
    List<Budget> getBudgetsByUserID(UUID userId);
}
