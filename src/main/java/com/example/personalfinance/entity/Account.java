package com.example.personalfinance.entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "accounts")
@Entity
@Data
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "current_balance")
    private double currentBalance;
    
    @Column(name = "payment_types")
    private String paymentTypes;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public List<String> getPaymentTypes() {
        return Arrays.asList(paymentTypes.split(", "));
    }

    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = String.join(", ", paymentTypes);
    }
}
