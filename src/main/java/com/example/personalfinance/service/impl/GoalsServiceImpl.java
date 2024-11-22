package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.GoalsRepository;
import com.example.personalfinance.service.GoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalsServiceImpl implements GoalsService {
    private final GoalsRepository goalsRepository;

    @Override
    public Goals createGoal(Goals goals) {
        return goalsRepository.save(goals);
    }
    @Override
    public Goals updateGoal(Long id, Goals updatedGoal) {
        Goals existingGoal = goalsRepository.findById(id).orElseThrow();
        existingGoal.setName(updatedGoal.getName());
        existingGoal.setDescription(updatedGoal.getDescription());
        existingGoal.setStatus(updatedGoal.getStatus());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        existingGoal.setTargetDate(updatedGoal.getTargetDate());
        return goalsRepository.save(existingGoal);
    }

    @Override
    public void deleteGoal(Long id) {
       Optional<Goals> existingGoal = goalsRepository.findById(id);
       if(existingGoal.isPresent()) {
           goalsRepository.deleteById(id);
       }else {
           throw new IllegalArgumentException("Goal with id " + id + " not exist.");
       }
    }

    @Override
    public Optional<Goals> getGoal(Long id) {
        return goalsRepository.findById(id);
    }

    @Override
    public List<Goals> getAllGoalsByUser(User user) {
        return goalsRepository.findAllByUser(user);
    }
}
