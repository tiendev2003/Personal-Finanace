package com.example.personalfinance.bean.request;

import lombok.Data;

@Data
public class TransactionRequest {
    private double amount;
    private String description;
    private String paymentType;
    private Integer categoryId;
    private Integer accountId;
    private Long dateTime;
}
