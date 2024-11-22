package com.example.personalfinance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final JWTGenerator jwtGenerator;

    @GetMapping("/monthly-data")
    public ResponseEntity<BaseResponse> getMonthlyData(@RequestHeader(value ="Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Map<String, Object>> data = dashboardService.getMonthlyData(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }

    @GetMapping("/this-month/expenses")
    public ResponseEntity<BaseResponse> getThisMonthExpenses(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Map<String, Object>> data = dashboardService.getThisMonthExpenses(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }
    @GetMapping("/this-month/income")
    public ResponseEntity<BaseResponse> getThisMonthIncome(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        List<Map<String, Object>> data = dashboardService.getThisMonthIncome(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }

    @GetMapping("/this-month/total/income-and-expenses")
    public ResponseEntity<BaseResponse> getThisMonthTotalIncomeAndExpenses(@RequestHeader(value = "Authorization") String token)
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        Map<String, Object> data = dashboardService.getThisMonthTotalIncomeAndExpenses(userName);
        return ResponseEntity.ok(new BaseResponse("success", data));
    }
}
