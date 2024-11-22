package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServeImpl implements DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Map<String, Object>> getMonthlyData(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return convertMonthlyData(transactionRepository.getMonthlyData(user.getUserId()));
    }

    @Override
    public List<Map<String, Object>> convertMonthlyData(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("month", o[0]);
            temp.put("expenses", o[1]);
            temp.put("income", o[2]);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> convertThisMonthExpenses(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("category", o[0]);
            temp.put("expenses", o[1]);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> convertThisMonthIncome(List<Object[]> queryResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("category", o[0]);
            temp.put("income", o[1]);
            result.add(temp);
        }
        return result;
    }

    @Override
    public Map<String, Object> convertThisMonthTotalIncomeAndExpenses(List<Object[]> queryResult) {
        Object[] row = queryResult.get(0);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total expenses", row[0]);
        result.put("total income", row[1]);
        return result;
    }

    @Override
    public List<Map<String, Object>> getThisMonthExpenses(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return convertThisMonthExpenses(transactionRepository.getThisMonthExpenses(user.getUserId()));
    }

    @Override
    public List<Map<String, Object>> getThisMonthIncome(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return convertThisMonthIncome(transactionRepository.getThisMonthIncome(user.getUserId()));
    }

    @Override
    public Map<String, Object> getThisMonthTotalIncomeAndExpenses(String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        return convertThisMonthTotalIncomeAndExpenses(transactionRepository.getThisMonthTotalIncomeAndExpenses(user.getUserId()));
    }
}
