package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {
    List<Goals> findAllByUser(User user);
}
