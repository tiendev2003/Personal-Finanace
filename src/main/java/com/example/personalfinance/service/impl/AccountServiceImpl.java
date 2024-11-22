package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.AccountRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.AccountService;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Lazy
    private TransactionService transactionService;
    
    @Autowired
    public void setTransactionService(@Lazy TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public boolean hasAccount(String accountId) {
        try {
            Account entity = accountRepository.getOne(Integer.valueOf(accountId));
            return entity.getAccountId() == Integer.parseInt(accountId);
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(String username, String accountId) {
        try {
            User user = userRepository.findByEmail(username).orElseThrow();
            Account entity = accountRepository.getOne(Integer.valueOf(accountId));
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public void debitBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void creditBalance(Account account, double amount) {
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(Account account, Integer accountId) {
        Account acc = accountRepository.findById(accountId).orElseThrow();
        acc.setCurrentBalance(account.getCurrentBalance());
        acc.setName(account.getName());
        acc.setPaymentTypes(account.getPaymentTypes());
        accountRepository.save(acc);
    }

    @Override
    public void addAccount(Account account, String userName) {
        try{
            User user = userRepository.findByEmail(userName).orElseThrow();
            account.setUser(user);
            accountRepository.save(account);
        }catch(Exception ignored){

        }
    }

    @Override
    public void deleteAccount(String accountId) {
        try{
            Account entity = accountRepository.findById(Integer.valueOf(accountId)).orElseThrow();
            entity.setDeleted(true);
            accountRepository.save(entity);
        }catch (Exception ignored){
            // Handle exception
        }
    }

    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        try{
            User user = userRepository.findByEmail(username).orElseThrow();
            List<Account> accountList = accountRepository.findAllByUserAndIsDeletedFalse(user);
            List<AccountResponse> accountResponseList = new ArrayList<>();
            for (Account account : accountList) {
                double totalExpenses = 0;
                double totalIncome = 0;
            List<Transaction> transactionList = transactionService.getTransactionsByAccount(account);
            for (Transaction transaction : transactionList) {
                    if(transaction.getCategory().getType().equals("expense")){
                        totalExpenses += transaction.getAmount();
                    } else if (transaction.getCategory().getType().equals("income")) {
                        totalIncome += transaction.getAmount();
                    }
            }
                AccountResponse accountResponse = new AccountResponse(
                    account.getAccountId(),
                    account.getName(),
                    account.getCurrentBalance(),
                    account.getPaymentTypes(),
                    totalExpenses,
                    totalIncome
            );
            accountResponseList.add(accountResponse);
            }
            return accountResponseList;
        }catch(Exception e ){
            return null;
        }
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return accountList;
    }
}
