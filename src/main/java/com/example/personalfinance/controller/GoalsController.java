package com.example.personalfinance.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Goals;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.GoalsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goals")
public class GoalsController {
    private final GoalsService goalsService;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;

    //API EndPoint for creating a Goal    
    @PostMapping
    public ResponseEntity<BaseResponse> createGoal(@RequestHeader(value = "Authorization") String token,
                                                   @RequestBody Goals goal)
    {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        goal.setUser(user);
        Goals createdGoal = goalsService.createGoal(goal);
        return ResponseEntity.ok(new BaseResponse("success", createdGoal));
    }

    //API EndPoint for Updating the existing a Goal
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateGoal(@PathVariable("id") Long id,
                                                   @RequestBody Goals goal)
    {
        Goals updatedGoal = goalsService.updateGoal(id, goal);
        return ResponseEntity.ok(new BaseResponse("success", updatedGoal));
    }

    //API EndPoint for Deleting the existing Goal
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteGoal(@PathVariable("id") Long id)
    {
        goalsService.deleteGoal(id);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    //API EndPoint for fetching a particular existing Goal
    @GetMapping("/{id}")
    public ResponseEntity<Goals> getGoal(@PathVariable("id") Long id)
    {
        Optional<Goals> goal = goalsService.getGoal(id);
        if(goal.isPresent()){
//            return new ResponseEntity<>(goal.get(), HttpStatus.OK);
            return ResponseEntity.ok(goal.get());
        }else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }

    //API EndPoint for fetching all the existing goals
    @GetMapping
    public ResponseEntity<BaseResponse> getAllGoals(@RequestHeader(value = "Authorization", defaultValue ="") String token)
    {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        List<Goals> goals = goalsService.getAllGoalsByUser(user);
        return ResponseEntity.ok(new BaseResponse("success", goals));
    }

    
    @GetMapping("/test")
    public ResponseEntity<List<Goals>> getAllTesGoals(){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<List<Goals>> getTestGoalsById(@PathVariable Integer id){
        List<Goals> goals = new ArrayList<>();
        return ResponseEntity.ok(goals);
    }
}
