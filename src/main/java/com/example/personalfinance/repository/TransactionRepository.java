package com.example.personalfinance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
        List<Transaction> findAllByUser(User user);

        List<Transaction> findAllByAccount(Account account);

        @Query(value = "select * from transactions where category_id =?1", nativeQuery = true)
        List<Transaction> findByCategory(Integer id);

        @Query(value = "SELECT " +
                        "    subquery.month, " +
                        "    COALESCE(expenses, 0) AS expenses, " +
                        "    COALESCE(income, 0) AS income " +
                        "FROM (" +
                        "    SELECT " +
                        "        MONTHNAME(DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL n.num MONTH)) AS month, " +
                        "        ROW_NUMBER() OVER (ORDER BY n.num DESC) AS rn " +
                        "    FROM " +
                        "        (SELECT 0 AS num UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) AS n "
                        +
                        "    ) AS subquery " +
                        "LEFT JOIN ( " +
                        "    SELECT " +
                        "        MONTHNAME(FROM_UNIXTIME(t.date_time/1000)) AS month, " +
                        "        SUM(CASE WHEN t.payment_type = 'expense' THEN t.amount ELSE 0 END) AS expenses, " +
                        "        SUM(CASE WHEN t.payment_type = 'income' THEN t.amount ELSE 0 END) AS income " +
                        "    FROM transactions t " +
                        "    JOIN categories c ON t.category_category_id = c.category_id " +
                        "    WHERE " +
                        "        t.user_id = ?1 AND " +
                        "        FROM_UNIXTIME(t.date_time/1000) >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 5 MONTH) "
                        +
                        "    GROUP BY month " +
                        ") AS data ON subquery.month = data.month " +
                        "ORDER BY subquery.rn DESC;", nativeQuery = true)
        List<Object[]> getMonthlyData(Integer userId);

        @Query(value = "SELECT " +
                        "c.category_name AS category, " +
                        "COALESCE(SUM(t.amount), 0) AS expenses " +
                        "FROM " +
                        "transactions t " +
                        "JOIN categories c ON t.category_category_id = c.category_id " +
                        "WHERE " +
                        "t.user_id = ?1 " +
                        "AND c.category_type = 'expense' " +
                        "AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW()) " +
                        "AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW()) " +
                        "GROUP BY " +
                        "c.category_name " +
                        "ORDER BY " +
                        "expenses DESC;", nativeQuery = true)
        List<Object[]> getThisMonthExpenses(Integer userId);

        @Query(value = "SELECT " +
                        "c.category_name AS category, " +
                        "COALESCE(SUM(t.amount), 0) AS income " +
                        "FROM " +
                        "transactions t " +
                        "JOIN categories c ON t.category_category_id = c.category_id " +
                        "WHERE " +
                        "t.user_id = ?1 " +
                        "AND c.category_type = 'income' " +
                        "AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW()) " +
                        "AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW()) " +
                        "GROUP BY " +
                        "c.category_name " +
                        "ORDER BY " +
                        "income DESC;", nativeQuery = true)
        List<Object[]> getThisMonthIncome(Integer userId);

        @Query(value = "SELECT " +
                        "    COALESCE(SUM(CASE WHEN c.category_type = 'expense' THEN t.amount END), 0) AS total_expenses, "
                        +
                        "    COALESCE(SUM(CASE WHEN c.category_type = 'income' THEN t.amount END), 0) AS total_income "
                        +
                        "FROM " +
                        "    transactions t " +
                        "    JOIN categories c ON t.category_id = c.category_id " + // Sửa từ `t.category_category_id`
                                                                                    // thành `t.category_id`
                        "WHERE " +
                        "    t.user_id = ?1 " +
                        "    AND MONTH(FROM_UNIXTIME(t.date_time/1000)) = MONTH(NOW()) " +
                        "    AND YEAR(FROM_UNIXTIME(t.date_time/1000)) = YEAR(NOW());", nativeQuery = true)
        List<Object[]> getThisMonthTotalIncomeAndExpenses(Integer userId);

}
