package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query(value = "SELECT budgets.id, budgets.amount, budgets.category_id, budgets.user_id, " +
    "(SELECT SUM(transactions.amount) " +
    "FROM transactions " +
    "WHERE transactions.user_id = ?1 " +
    "AND budgets.category_id = transactions.category_category_id " +
    "AND transactions.date_time >= UNIX_TIMESTAMP('2023-07-01 00:00:00') * 1000 " +
    "AND transactions.date_time <= UNIX_TIMESTAMP('2023-07-31 23:59:59') * 1000) AS used, " +
    "(budgets.amount - (SELECT SUM(transactions.amount) " +
    "FROM transactions " +
    "WHERE transactions.user_id = ?1 " +
    "AND budgets.category_id = transactions.category_category_id " +
    "AND transactions.date_time >= UNIX_TIMESTAMP('2023-07-01 00:00:00') * 1000 " +
    "AND transactions.date_time <= UNIX_TIMESTAMP('2023-07-31 23:59:59') * 1000)) AS balance " +
    "FROM budgets " +
    "WHERE budgets.user_id = ?1", nativeQuery = true)
List<Object[]> findAllByUser(Integer user_id);


}
