package com.example.personalfinance.service;

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BudgetService {
    List<Budget> getAllBudgets();
    
    List<Budget> getAllBudgetByUser(User user);

    Optional<Budget> getBudgetById(Long id);

    Budget createBudget(BudgetRequest budgetRequest, String userName);

    Budget updateBudget(Budget budget);

    void deleteBudget(Long id);

    boolean hasAlready(String userName, int categoryId);
}
