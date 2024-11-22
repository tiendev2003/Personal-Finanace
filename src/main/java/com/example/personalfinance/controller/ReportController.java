package com.example.personalfinance.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.service.TransactionService;
import com.example.personalfinance.util.TransactionExcelExporter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final JWTGenerator jwtGenerator;
    private final TransactionService transactionService;

    @GetMapping("/transaction/excel")
    public void transactionReportExcel(@RequestHeader(value = "Authorization") String token, HttpServletResponse httpServletResponse) throws IOException
    {
        String userName = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        httpServletResponse.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);

        List<Transaction> transactionList = transactionService.getTransactionsByUserName(userName);
        //Write to file excel
       TransactionExcelExporter transactionExcelExporter = new TransactionExcelExporter(transactionList);
       transactionExcelExporter.export(httpServletResponse);
    }
}
