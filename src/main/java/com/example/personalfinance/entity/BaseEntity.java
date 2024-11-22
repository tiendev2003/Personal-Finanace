package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "deleted", columnDefinition = "Bit(1) default false")
    private boolean isDeleted = false;
    
    @Column(updatable = false)
    @CreationTimestamp
    public LocalDateTime createdAt;
    
    @Column(updatable = true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    public boolean getIsDeleted() {
//        return isDeleted;
//    }
//
//    public void setIsDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}
