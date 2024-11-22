package com.example.personalfinance.service;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.request.ProfilePasswordRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.User;

import jakarta.mail.MessagingException;

@Service
public interface UserService {

    void updateUserProfileImage(ProfileImgRequest profileImg, String userName);

    void updateUserProfileName(ProfileNameRequest profileName, String userName);

    void updateUserProfileEmail(ProfileEmailRequest profileEmail, String userName);

    void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException;

    void newPassword(String email, String password);
    
    ResponseEntity<BaseResponse> updatePassord(ProfilePasswordRequest profilePassword, String userName);

    ResponseEntity<BaseResponse> register(User user);
   
    ResponseEntity<BaseResponse> login(LoginRequest user);
     ResponseEntity<BaseResponse> getUserProfile(String token);
    
}
