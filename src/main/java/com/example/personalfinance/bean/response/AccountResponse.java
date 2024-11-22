package com.example.personalfinance.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class AccountResponse {
    private int accountId;
    private String name;
    private double currentBalance;
    private String paymentTypes;
    private double totalExpense;
    private double totalIncome;

    public AccountResponse(int accountId, String name, double currentBalance, List<String> paymentTypes, double totalExpense, double totalIncome){
        this.accountId = accountId;
        this.name = name;
        this.currentBalance = currentBalance;
        this.paymentTypes = String.join(", ", paymentTypes);
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
    }
}
