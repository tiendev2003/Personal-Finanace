package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByUser(User user);
    List<Account> findAllByUserAndIsDeletedFalse(User user);
    
}
