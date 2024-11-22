package com.example.personalfinance.util;

import com.example.personalfinance.entity.Account;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.AccountRepository;
import com.example.personalfinance.repository.CategoryRepository;
import com.example.personalfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CategoryRepository categoryRepository;
  private final AccountRepository accountRepository;
  static User savedUser;
  
  public void saveUser(){
    User user = new User();
    user.setEmail("test@gmail.com");
    user.setPassword(passwordEncoder.encode("123"));
    user.setFirstName("Test-Account");
    user.setLastName("1");
    savedUser = userRepository.save(user);
  }
  
  public void saveCategory(User user, String name, String type){
    Category category = new Category();
    category.setUserId(user);
    category.setName(name);
    category.setType(type);
    categoryRepository.save(category);
  }
  
  public void saveAccount(User user, String name, List<String> paymentType){
    Account account = new Account();
    account.setUser(user);
    account.setName(name);
    account.setPaymentTypes(paymentType);
    account.setCurrentBalance(5000);
    accountRepository.save(account);
  }

  @Override
  public void run(String... args) throws Exception {
    if(!userRepository.existsByEmail("test@gmail.com")){
      saveUser();
      if(savedUser != null){
        //Expenses Category
        saveCategory(savedUser,"Food","expense");
        saveCategory(savedUser,"Groceries","expense");
        saveCategory(savedUser,"Rent","expense");
        saveCategory(savedUser,"Utilities","expense");
        saveCategory(savedUser,"Debt Payments","expense");

        //Income Category
        saveCategory(savedUser,"Salary","income");
        saveCategory(savedUser,"Investment Income","income");
        saveCategory(savedUser,"Freelance ","income");
        saveCategory(savedUser,"Commission and Bonuses","income");
        saveCategory(savedUser,"Annuities ","income");

        //Account Create
        saveAccount(savedUser,"Cash", List.of("other"));
      }
    }
  }
}
