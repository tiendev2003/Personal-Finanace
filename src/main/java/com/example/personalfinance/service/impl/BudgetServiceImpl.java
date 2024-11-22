package com.example.personalfinance.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.BudgetRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> getAllBudgetByUser(User user) {
        List<Object[]> results = budgetRepository.findAllByUser(user.getUserId());

        return results.stream().map(result -> {
            Budget budget = new Budget();

            budget.setAmount((Double) result[1]);
            budget.setCategory(categoryService.getCategoryById((Integer) result[2]));
            budget.setUser(user);
            budget.setUsed( Integer.toUnsignedLong((Integer) result[3]));
            budget.setBalance((Long) result[4]);
            return budget;
        }).toList();
    }

    @Override
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    @Override
    public Budget createBudget(BudgetRequest budgetRequest, String userName) {
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
        User user = userRepository.findByEmail(userName).orElseThrow();
        Budget budget = new Budget(category, budgetRequest.getAmount(), user, 0L, 0L);
        return budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public boolean hasAlready(String userName, int categoryId) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        List<Object[]> results = budgetRepository.findAllByUser(user.getUserId());

        List<Budget> budgetList = results.stream().map(result -> {
            Budget budget = new Budget();

            budget.setAmount((Double) result[1]);
            budget.setCategory(categoryService.getCategoryById((Integer) result[2]));
            budget.setUser(user);
            budget.setUsed(Integer.toUnsignedLong((Integer) result[3]));
            budget.setBalance((Long) result[4]);
            return budget;
        }).toList();
        boolean isAlready = false;
        for (Budget b : budgetList) {
            if (b.getCategory().getCategoryId() == categoryId) {
                isAlready = true;
                break;
            }
        }
        return isAlready;
    }
}
