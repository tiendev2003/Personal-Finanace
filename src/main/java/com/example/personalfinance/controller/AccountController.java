package com.example.personalfinance.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.response.AccountResponse;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Account;
import com.example.personalfinance.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final JWTGenerator jwtGenerator;
    private final AccountService accountService;

    @PostMapping
    public BaseResponse createAccount(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody Account account)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        accountService.addAccount(account, userName);
        return new BaseResponse("success");
    }

    @PutMapping
    public BaseResponse updateAccount(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody Account account,
                                      @RequestParam String accountId)
    {
        accountService.updateAccount(account, Integer.valueOf(accountId));
        return new BaseResponse("success");
    }

    @GetMapping
    public BaseResponse getAccount(@RequestHeader(value = "Authorization", defaultValue ="") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<AccountResponse> accounts = accountService.getAccountsByUsername(userName);
        return new BaseResponse("success", accounts);
    }
    
    @DeleteMapping
    public BaseResponse deleteAccount(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam String accountId)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        if(accountService.hasAccount(accountId)){
            if(accountService.hasPermission(userName, accountId)){
                accountService.deleteAccount(accountId);
                return new BaseResponse("success");
            }else{
                return new BaseResponse("couldn't delete account");
            }
        }else{
            return new BaseResponse("account not found");
        }
    }
//Test case ?
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccount(){
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
//Test case ?
    @GetMapping("/{id}")
    public ResponseEntity<List<Account>> getAccountById(@PathVariable Integer id){
        List<Account> accounts = new ArrayList<>();
        return ResponseEntity.ok(accounts);
    }
}
