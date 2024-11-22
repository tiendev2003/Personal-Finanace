package com.example.personalfinance.service;

import com.example.personalfinance.entity.Debt;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional // add cái annotation này là j thế 
public interface DebtService {
    Debt debtCreate(Debt deb, String uName);
    Debt debtUpdate(Debt deb, Integer debtId);
    Debt debGetId(Integer dId);
    String debtDelete(Integer dId);
    List<Debt> debGet(String uName, Integer value);
    List<Debt> getAllDebts();
    Date parseDueDate(String dueDate);

}
