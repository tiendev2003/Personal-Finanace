package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Table(name="transactions")
@Entity
@Data
public class Transaction extends BaseEntity implements Comparable<Transaction>   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "amount")
    private double amount;
    @Column(name = "description")
    private String description;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "date_time")
    private Long dateTime;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn()
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Transaction(double amount, String description, String paymentType, Long dateTime, Category category, Account account, User user) {
        this.amount = amount;
        this.description = description;
        this.paymentType = paymentType;
        this.dateTime = dateTime;
        this.category = category;
        this.account = account;
        this.user = user;
    }

    public Transaction() {

    }

    @Override
    public int compareTo(Transaction other) {
        return this.createdAt.compareTo(other.getCreatedAt());
    }
}
