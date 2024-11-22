package com.example.personalfinance.service.impl;

import com.example.personalfinance.bean.request.TransactionRequest;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.AccountService;
import com.example.personalfinance.service.CategoryService;
import com.example.personalfinance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private  AccountService accountService;
    
    @Autowired
    public void setAccountService(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }
    
    @Override
    public List<Transaction> getTransactionsByUserName(String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            List<Transaction> transactionList = transactionRepository.findAllByUser(user);
            transactionList.sort(Collections.reverseOrder());
            return transactionList;
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        try {
            return transactionRepository.findAllByAccount(account);
        } catch (UsernameNotFoundException e) {
           return null;
        }
    }

    @Override
    public void addTransaction(TransactionRequest transactionRequest, String userName) {
        Account account = accountService.getAccountById(transactionRequest.getAccountId());
        Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());
        User user = userRepository.findByEmail(userName).orElseThrow();
        Transaction transaction = new Transaction(
                transactionRequest.getAmount(),
                transactionRequest.getDescription(),
                transactionRequest.getPaymentType(),
                transactionRequest.getDateTime(),
                category,
                account,
                user
        );
        transactionRepository.save(transaction);
        if(category.getType().equals("expense")){
            accountService.debitBalance(account, transactionRequest.getAmount());
        }else if(category.getType().equals("income")){
            accountService.creditBalance(account, transactionRequest.getAmount());
        }
    }

    @Override
    public void updateTransaction(TransactionRequest transactionRequest, Integer transactionId, String userName) {
        try{
            Transaction entity = transactionRepository.findById(transactionId).orElseThrow();
            Account account = accountService.getAccountById(transactionRequest.getAccountId());
            Category category = categoryService.getCategoryById(transactionRequest.getCategoryId());
            entity.setAccount(account);
            entity.setCategory(category);
            entity.setDateTime(transactionRequest.getDateTime());
            entity.setPaymentType(transactionRequest.getPaymentType());
            entity.setDescription(transactionRequest.getDescription());
            entity.setAmount(transactionRequest.getAmount());
            transactionRepository.save(entity);
        }catch (Exception ignore) {

        }
    }

    @Override
    public void deleteTransaction(int id) {
        try {
            Transaction entity = transactionRepository.findById(id).orElseThrow();
            transactionRepository.delete(entity);
        }catch (Exception ignored){

        }
    }

    @Override
    public boolean hasTransaction(String transactionId) {
        try {
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId)).orElseThrow();
            return entity.getId() == Integer.parseInt(transactionId);
        }catch (Exception ignored){
            return false;
        }
    }

    @Override
    public boolean hasPermission(String userName, String transactionId) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            Transaction entity = transactionRepository.findById(Integer.valueOf(transactionId)).orElseThrow();
            return Objects.equals(entity.getUser().getUserId(), user.getUserId());
        }catch (Exception ignored){
            return false;
        }
    }
}
