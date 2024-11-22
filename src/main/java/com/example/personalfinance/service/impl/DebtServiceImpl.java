package com.example.personalfinance.service.impl;

import com.example.personalfinance.entity.Debt;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.DebtRepository;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.DebtService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    @Override
    public Debt debtCreate(Debt deb, String uName) {
        try {
            User user = userRepository.findByEmail(uName).orElseThrow();
            deb.setUser(user);
        } catch (Exception ignored) {

        }
        return debtRepository.save(deb);
    }

    @Override
    public Debt debtUpdate(Debt deb, Integer debtId) {
        Debt debt = debtRepository.findById(debtId).get();

        if (!"0".equalsIgnoreCase(String.valueOf(deb.getAmount()))){
            debt.setAmount(deb.getAmount());
        }if (Objects.nonNull(deb.getMoneyFrom()) && !"".equalsIgnoreCase(deb.getMoneyFrom())){
            debt.setMoneyFrom(deb.getMoneyFrom());
        }if (Objects.nonNull(deb.getStatus()) && !"".equalsIgnoreCase(deb.getStatus())){
            debt.setStatus(deb.getStatus());
        }if (Objects.nonNull(deb.getDueDate()) && !"".equalsIgnoreCase(deb.getDueDate())){
            debt.setDueDate(deb.getDueDate());
        }
        return debtRepository.save(debt);
    }

    @Override
    public Debt debGetId(Integer dId) {
        return debtRepository.findById(dId).get();
    }

    @Override
    public String debtDelete(Integer dId) {
        debtRepository.deleteById(dId);
        return "Deleted";
    }

    @Override
    public List<Debt> debGet(String uName, Integer value) {
        try {
            User user = userRepository.findByEmail(uName).orElseThrow();
            if (value == 1) {
                return debtRepository.findAllByUserOrderByAmountDesc(user);
            }else if (value == 2) {
                List<Debt> debts = debtRepository.findAllByUser(user);
                return debts.stream()
                        .sorted(Comparator.comparing(debt -> parseDueDate(debt.getDueDate())))
                        .collect(Collectors.toList());
            }
            return debtRepository.findAllByUser(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Debt> getAllDebts() {
        return debtRepository.findAll();
    }

    @Override
    public Date parseDueDate(String dueDate) {
       try {
           SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
           return formatter.parse(dueDate);
       }catch (ParseException e) {
            e.printStackTrace();
            return null;
       }
    }

}
