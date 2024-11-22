package com.example.personalfinance.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DashboardService {
    List<Map<String, Object>> getMonthlyData(String userName);

    List<Map<String, Object>> convertMonthlyData(List<Object[]> queryResult);

    List<Map<String, Object>> convertThisMonthExpenses(List<Object[]> queryResult);

    List<Map<String, Object>> convertThisMonthIncome(List<Object[]> queryResult);

    Map<String, Object> convertThisMonthTotalIncomeAndExpenses(List<Object[]> queryResult);

    List<Map<String, Object>> getThisMonthExpenses(String userName);

    List<Map<String, Object>> getThisMonthIncome(String userName);

    Map<String, Object> getThisMonthTotalIncomeAndExpenses(String userName);
}
