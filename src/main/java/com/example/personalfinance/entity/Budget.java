package com.example.personalfinance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "budgets")
@Entity
@Data
@NoArgsConstructor
public class Budget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "budget_status")
    private Long used;

    @Column(name = "budget_balance")
    private Long balance;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Custom constructor that excludes 'budgetId' because it is auto-generated
    public Budget(Category category, double amount, User user, Long used, Long balance) {
        this.category = category;
        this.amount = amount;
        this.user = user;
        this.used = used;
        this.balance = balance;
    }

    public Budget(Category category, double amount, User user) {
        this.category = category;
        this.amount = amount;
        this.user = user;
    }
}
