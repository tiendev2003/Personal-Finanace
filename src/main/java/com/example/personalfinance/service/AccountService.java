package com.example.personalfinance.service;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    boolean hasAccount(String accountId);

    boolean hasPermission(String username, String accountId);

    void debitBalance(Account account, double amount);

    void creditBalance(Account account, double amount);

    void updateAccount(Account account, Integer accountId);

    void addAccount(Account account, String userName);

    void deleteAccount(String accountId);

    List<AccountResponse> getAccountsByUsername(String username);

    Account getAccountById(Integer id);

    List<Account> getAllAccounts();
}
