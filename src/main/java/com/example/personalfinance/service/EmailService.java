package com.example.personalfinance.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface EmailService {
    void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException, MessagingException;
}
