package org.financk.financk_backend.budget.service;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.budget.models.Budget;
import org.financk.financk_backend.budget.models.BudgetItem;
import org.financk.financk_backend.budget.models.dto.BudgetDTO;
import org.financk.financk_backend.budget.models.dto.BudgetItemDTO;
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

    public ServiceResult<UserBudgetResult> getUserBudgets(UUID userId) {
        List<BudgetDTO> budgetsByUserID = budgetRepository.findBudgetsByUserId(userId)
                .stream().map((budget -> new BudgetDTO(budget.getId(), budget.getName())))
                .toList();
        return new ServiceResult<>(true,new UserBudgetResult("Budget for user " + userId,budgetsByUserID),null,0);
    }

    public ServiceResult<BudgetResult> getBudget(UUID budgetId) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            BudgetDTO budgetDto = new BudgetDTO(budget.getId(), budget.getName());
            budgetDto.setBudgetItems(budget.getBudgetItems());
            budgetDto.setStartingAmount(budget.getStartingAmount());
            budgetDto.setGoalAmount(budget.getGoalAmount());
            return new ServiceResult<>(true,new BudgetResult("Budget found.",budgetDto),null,0);
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);
        }
    }

    public ServiceResult<BudgetResult> addBudgetItemToBudget(UUID budgetId, BudgetItemDTO budgetItemDTO) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            BudgetItem budgetItem = new BudgetItem();
            budgetItem.setItemType(budgetItemDTO.getItemType());
            budgetItem.setItemDate(budgetItemDTO.getItemDate());
            budgetItem.setItemAmount(budgetItemDTO.getItemAmount());
            budget.getBudgetItems().add(budgetItem);
            budgetRepository.save(budget);
            return new ServiceResult<>(true,null,null,0);
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);
        }
    }

    public ServiceResult<String> deleteBudgetItem(UUID budgetId, UUID itemId) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            budget.getBudgetItems().removeIf(budgetItem -> budgetItem.getId().equals(itemId));
            budgetRepository.save(budget);
            return new ServiceResult<>(true,null,null,0);
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);
        }
    }

    public ServiceResult<String> updateBudgetItem(UUID budgetId, UUID itemId, BudgetItemDTO budgetItemDTO) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            Optional<BudgetItem> optionalBudgetItem = budget.getBudgetItems().stream().filter(budgetItem -> budgetItem.getId().equals(itemId)).findFirst();
            if (optionalBudgetItem.isPresent()) {
                BudgetItem budgetItem = optionalBudgetItem.get();
                if (budgetItemDTO.getItemAmount() != null) budgetItem.setItemAmount(budgetItemDTO.getItemAmount());
                if (budgetItemDTO.getItemDate() != null) budgetItem.setItemDate(budgetItemDTO.getItemDate());
                if (budgetItemDTO.getItemType() != null) budgetItem.setItemType(budgetItemDTO.getItemType());
                budgetRepository.save(budget);
                return new ServiceResult<>(true,null,null,0);
            }
            else {
                return new ServiceResult<>(false,null,"Budget Item not found",2);
            }
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);
        }
    }

    public ServiceResult<String> updateBudget(UUID budgetId, BudgetDTO budgetDTO) {
        Optional<Budget> optionalBudget = budgetRepository.findById(budgetId);
        if (optionalBudget.isPresent()) {
            Budget budget = optionalBudget.get();
            if (budgetDTO.getName() != null) budget.setName(budgetDTO.getName());
            if (budgetDTO.getStartingAmount() != null) budget.setStartingAmount(budgetDTO.getStartingAmount());
            if (budgetDTO.getGoalAmount() != null) budget.setGoalAmount(budgetDTO.getGoalAmount());
            budgetRepository.save(budget);
            return new ServiceResult<>(true,null,null,0);
        }
        else {
            return new ServiceResult<>(false,null,"Budget not found",1);
        }
    }
}
