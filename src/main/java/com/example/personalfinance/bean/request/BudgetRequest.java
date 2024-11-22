package com.example.personalfinance.bean.request;

import lombok.Data;

@Data
public class BudgetRequest {
    private int categoryId;
    private double amount;
}
