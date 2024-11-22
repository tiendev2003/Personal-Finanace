package com.example.personalfinance.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.request.ProfilePasswordRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final OTPStorage otpStorage;
    private final JavaMailSender mailSender;

    @Override
    public void updateUserProfileImage(ProfileImgRequest profileImg, String userName) {
        try {
            User user = userRepository.findByEmail(userName).orElseThrow();
            user.setProfileImage(profileImg.getImage().getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUserProfileName(ProfileNameRequest profileName, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setFirstName(profileName.getFirstName());
        user.setLastName(profileName.getLastName());
        userRepository.save(user);
    }

    @Override
    public void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        user.setEmail(profileEmail.getEmail());
        userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "PersonalFinance@outlook.com"; //not create a real email account right now
        String senderName = "Personal Finance Team";
        String subject = "Personal Finance Team account security code";
        String content = "<div>\n" +
                        "    <span style=\"color:#808080;padding: 2px;font-family: sans-serif;\">Paymint Account</span><br>\n" +
                        "    <span style=\"color:#5C6AC4;padding: 2px;font-size:32px;font-family: sans-serif;\"><b>Security code</b></span><br><br>\n" +
                        "    <span style=\"font-family: sans-serif;\">Please use the following security code for the Paymint account.</span><br><br><br>\n" +
                        "    <span style=\"font-family: sans-serif;\">Security code: <b>[[CODE]]</b></span><br><br><br>\n" +
                        "    <span style=\"font-family: sans-serif;\">Thanks,</span><br>\n" +
                        "    <span style=\"font-family: sans-serif;\">The Paymint Team</span>\n" +
                        "</div>";

    }

    @Override
    public void newPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    
    @Override
    public ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName) {
        User user = userRepository.findByEmail(userName).orElseThrow();
        //getOldPassword: request
        //getNewPassword: request
        //user.getPassword: database
        if(new BCryptPasswordEncoder().matches(profilePassword.getOldPassword(), user.getPassword())) {
            if(new BCryptPasswordEncoder().matches(profilePassword.getNewPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new BaseResponse("New Password can't be same as Old Password!", null));
            }
            user.setPassword(passwordEncoder.encode(profilePassword.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new BaseResponse("Password Updated Successfully", null));
        }
        return ResponseEntity.badRequest().body(new BaseResponse("Old Password does not match", null));
    }
    
    @Override
    public ResponseEntity<BaseResponse> register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Email Already Exists", null));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse("User Registered Successfully", user));
    }

    @Override
    public ResponseEntity<BaseResponse> login(LoginRequest user) {
        // Retrieve the user from the database
        User userEntity = userRepository.findByEmail(user.getEmail()).orElse(null);
        
        // Validate email and password
        if(userEntity == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.badRequest().body(new BaseResponse("Incorrect Email or Password...", null));
        }

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Dynamically update the secret for token generation
        String dynamicSecret = "dynamicSecretOf" + System.currentTimeMillis()
                + "UserID" + userEntity.getUserId()
                +  "withUserName" + userEntity.getFirstName() + userEntity.getLastName()
                + "secretToken"; // Example dynamic secret
      

        // Generate the JWT token
        String token = jwtGenerator.generateToken(authentication);
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token generation failed. Please check your secret and configuration.");
        }

        // Prepare the response data
        Map<Object, Object> data = new HashMap<>();
        data.put("token", token);

        System.out.println("Generated Token: " + token);
        
        // Return the response
        return ResponseEntity.ok(new BaseResponse("Login Success", data));
    }

    @Override
    public ResponseEntity<BaseResponse> getUserProfile(String token) {
        if (!jwtGenerator.validateToken(jwtGenerator.getTokenFromHeader(token))) {
            return ResponseEntity.badRequest().body(new BaseResponse("Invalid Token", null));
        }
        String email = jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token));
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new BaseResponse("User profile retrieved successfully", user));
    }
}
