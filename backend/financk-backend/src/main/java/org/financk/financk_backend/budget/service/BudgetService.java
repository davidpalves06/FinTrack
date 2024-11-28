package org.financk.financk_backend.budget.service;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.budget.models.Budget;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetResult;
import org.financk.financk_backend.budget.models.dto.UserBudgetResult;
import org.financk.financk_backend.budget.repository.BudgetRepository;
import org.financk.financk_backend.common.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BudgetService {
    private static final String LOG_TITLE = "[BudgetService] -";
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public ServiceResult<BudgetResult> createBudget(BudgetDTO budgetDTO) {
        Budget newBudget = new Budget();
        newBudget.setUserID(budgetDTO.getUserID());
        newBudget.setBudgetName(budgetDTO.getBudgetName());
        newBudget.setMonthlyStartingBalance(budgetDTO.getMonthlyStartingBalance());

        budgetRepository.save(newBudget);

        return new ServiceResult<>(true,new BudgetResult("Budget created",null),null,0);
    }

    public ServiceResult<UserBudgetResult> getUserBudgets(UUID userId) {
        List<BudgetDTO> budgetsByUserID = budgetRepository.getBudgetsByUserID(userId)
                .stream().map((budget -> new BudgetDTO(budget.getUserID(), budget.getBudgetName())))
                .toList();
        return new ServiceResult<>(true,new UserBudgetResult("Budget for user " + userId,budgetsByUserID),null,0);
    }

    public ServiceResult<BudgetResult> getBudget(UUID budgetId) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            BudgetDTO budgetDto = new BudgetDTO(budget.getUserID(), budget.getBudgetName());
            return new ServiceResult<>(true,new BudgetResult("Budget found.",budgetDto),null,0);
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);

        }
    }

    public ServiceResult<BudgetResult> deleteBudget(UUID budgetId) {
        budgetRepository.deleteById(budgetId);
        return new ServiceResult<>(true,new BudgetResult("Budget deleted",null),null,0);
    }
}
