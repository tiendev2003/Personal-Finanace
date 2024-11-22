package com.example.personalfinance.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.personalfinance.bean.request.BudgetRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Budget;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.BudgetService;
import com.example.personalfinance.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final JWTGenerator jwtGenerator;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllBudgets(@RequestHeader(value = "Authorization") String token) {
        User user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token))).orElseThrow();
        List<Budget> budgets = budgetService.getAllBudgetByUser(user);
        return ResponseEntity.ok(new BaseResponse("success", budgets));
    }

    //API EndPoint for fetching a particular Budget
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
//        return new ResponseEntity<>(budget, HttpStatus.OK);
        return ResponseEntity.ok(budget);

    }


    //API EndPoint for creating a Budget
    @PostMapping
    public ResponseEntity<BaseResponse> createBudgets(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody BudgetRequest budgetRequest) {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if (!budgetService.hasAlready(userName, budgetRequest.getCategoryId())) {
            Budget createdBudget = budgetService.createBudget(budgetRequest, userName);
            return new ResponseEntity<>(new BaseResponse("success", createdBudget), HttpStatus.CREATED);

        } else {
            return ResponseEntity.ok(new BaseResponse("Already exist"));
        }
    }

    //API EndPoint for Updating an existing Budget
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateBudget(@PathVariable("id") Long id,
                                                     @RequestBody BudgetRequest budgetRequest) {
        Budget existingBudget = budgetService.getBudgetById(id).orElse(null);
        if (existingBudget == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
        Category category = categoryService.getCategoryById(budgetRequest.getCategoryId());
        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetRequest.getAmount());
        budgetService.updateBudget(existingBudget);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    //API EndPoint for Deleting an existing Budget    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBudget(@PathVariable("id") Long id) {
        Budget budget = budgetService.getBudgetById(id).orElse(null);
        if (budget == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    //test case
    @GetMapping("/budget")
    public ResponseEntity<List<Account>> getAllBudget() {
        List<Account> accounts = new ArrayList<>();
        List<Budget> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/budget/{id}")
    public ResponseEntity<List<Account>> getBudgetById(@PathVariable Integer id) {
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }
}
