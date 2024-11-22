package com.example.personalfinance.service.impl;

import com.example.personalfinance.service.EmailService;
import com.example.personalfinance.util.OTPStorage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final OTPStorage otpStorage;
    
    @Override
    public void sendVerificationEmail(String email) throws MailException, UnsupportedEncodingException, MessagingException {
        String fromAddress = "bach.nt.2150@aptechlearning.edu.vn";
        String senderName = "Personal Finance Team";
        String subject = "Verification Email";
        String content = "<div>\n" +
                "    <span style=\"color:#808080;padding: 2px;font-family: sans-serif;\">Paymint Account</span><br>\n" +
                "    <span style=\"color:#5C6AC4;padding: 2px;font-size:32px;font-family: sans-serif;\"><b>Security code</b></span><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Please use the following security code for the Paymint account.</span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Security code: <b>[[CODE]]</b></span><br><br><br>\n" +
                "    <span style=\"font-family: sans-serif;\">Thanks,</span><br>\n" +
                "    <span style=\"font-family: sans-serif;\">The Paymint Team</span>\n" +
                "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);

        String code = otpStorage.generateOTP(email);  // This generates and returns a unique OTP
        content = content.replace("[[CODE]]", code);

        helper.setText(content, true);  // Set to `true` for HTML content

        mailSender.send(message);
    }
}
