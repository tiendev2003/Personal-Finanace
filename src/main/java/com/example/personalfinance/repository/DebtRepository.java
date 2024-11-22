package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.entity.User;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Integer> {
    List<Debt> findAllByUser(User user);
    List<Debt> findAllByUserOrderByDueDateAsc(User user);
    List<Debt> findAllByUserOrderByAmountDesc(User user);
}
