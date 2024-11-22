package com.example.personalfinance.util;

import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OTPStorage {
  private final Map<String, String> otpMap = new ConcurrentHashMap<>();
  private static final int OTP_Length = 6;

  public String generateOTP(String email){
    String otp = generateRandomOTP();
    otpMap.put(email, otp);
    return otp;
  }

  public String getOTP(String email){
    return otpMap.get(email);
  }

  public void removeOTP(String email){
    otpMap.remove(email);
  }

  private String generateRandomOTP(){
    int otpInt = new Random().nextInt((int) Math.pow(10, OTP_Length));
    return String.format("%0" + OTP_Length + "d", otpInt);
  }
}
