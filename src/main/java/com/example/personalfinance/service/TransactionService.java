package com.example.personalfinance.service;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<Transaction> getTransactionsByUserName(String userName);

    List<Transaction> getTransactionsByAccount(Account account);

    void addTransaction(TransactionRequest transactionRequest, String userName);

    void updateTransaction(TransactionRequest transactionRequest, Integer transactionId, String userName);

    void deleteTransaction(int id);

    boolean hasTransaction(String transactionId);

    boolean hasPermission(String userName, String transactionId);


}
