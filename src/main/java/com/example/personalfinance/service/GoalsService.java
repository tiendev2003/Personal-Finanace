package com.example.personalfinance.service;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GoalsService {
    Goals createGoal(Goals goals);

    Goals updateGoal(Long id, Goals goals);

    void deleteGoal(Long id);

    Optional<Goals> getGoal(Long id);

    List<Goals> getAllGoalsByUser(User user);
}
