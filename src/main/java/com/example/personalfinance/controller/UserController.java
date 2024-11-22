package com.example.personalfinance.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinance.bean.request.LoginRequest;
import com.example.personalfinance.bean.request.ProfileEmailRequest;
import com.example.personalfinance.bean.request.ProfileImgRequest;
import com.example.personalfinance.bean.request.ProfileNameRequest;
import com.example.personalfinance.bean.request.ProfilePasswordRequest;
import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.config.auth.JWTGenerator;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.UserRepository;
import com.example.personalfinance.service.EmailService;
import com.example.personalfinance.service.UserService;
import com.example.personalfinance.util.OTPStorage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private OTPStorage otpStorage;
    @Autowired
    private EmailService emailService;

    public UserController(UserRepository userRepository, JWTGenerator jwtGenerator, UserService userService, OTPStorage otpStorage, EmailService emailService) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
        this.otpStorage = otpStorage;
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "tien";
    }
    

    @PostMapping("/auth/register")
    public ResponseEntity<BaseResponse> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest user) {
        return userService.login(user);
    }

    @GetMapping("/auth/validateToken")
    public ResponseEntity<BaseResponse> home(@RequestHeader(value = "Authorization") String token) {
        Map<Object, Object> data = new HashMap<>();
        if (jwtGenerator.validateToken(jwtGenerator.getTokenFromHeader(token))) {
            Optional<User> user = userRepository.findByEmail(jwtGenerator.getUsernameFromJWT(jwtGenerator.getTokenFromHeader(token)));
            data.put("user", user);
            return ResponseEntity.ok(new BaseResponse("success", data));
        }
        return new ResponseEntity<>(new BaseResponse("Session Expired", data), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/profile/image")
    public ResponseEntity<BaseResponse> updatedProfilePicture(@RequestHeader(value = "Authorization") String token,
                                                              @ModelAttribute ProfileImgRequest profileImgRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileImage(profileImgRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileImgRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile image.", e));
        }
    }

    @PostMapping("/profile/name")
    public ResponseEntity<BaseResponse> updateProfileName(@RequestHeader(value = "Authorization") String token,
                                                          @RequestBody ProfileNameRequest profileNameRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileName(profileNameRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileNameRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user profile name.", e));
        }
    }

    @PostMapping("/profile/email")
    public ResponseEntity<BaseResponse> updateProfileEmail(@RequestHeader(value = "Authorization") String token,
                                                           @RequestBody ProfileEmailRequest profileEmailRequest) {
        try {
            String username = jwtGenerator.getUsernameFromJWT(token);
            userService.updateUserProfileEmail(profileEmailRequest, username);
            return ResponseEntity.ok(new BaseResponse("success", profileEmailRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Fail to update user email.", e));
        }
    }

    @PostMapping("/auth/send-verification-email")
    public ResponseEntity<BaseResponse> sendVerificationEmail(@RequestParam(value = "email") String email) {
        try {
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(new BaseResponse("User already in use"));
            }
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok(new BaseResponse("success"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse("Fail, try again.", e));
        }
    }
    
    @PostMapping("/auth/verify-security-code")
    public ResponseEntity<BaseResponse> verifyOTP(@RequestParam(value = "email") String email, 
                                                  @RequestParam(value = "otp") String otp){
        String storeOTP = otpStorage.getOTP(email);
        if(storeOTP == null || !storeOTP.equals(otp)){
            return ResponseEntity.badRequest().body(new BaseResponse("Invalid OTP."));
        }
        otpStorage.removeOTP(email);
        return ResponseEntity.ok(new BaseResponse("OTP verified successfully"));
    }
    
    @PostMapping("/auth/forgot-password/send-verification-email")
    public ResponseEntity<BaseResponse> forgetPasswordSendVerificationEmail(@RequestParam(value = "email") String email){
        try{
            emailService.sendVerificationEmail(email);
            return ResponseEntity.ok(new BaseResponse("success"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new BaseResponse("Fail, try again.", e));
        }
    }
    
    @PutMapping("/auth/new-password")
    public ResponseEntity<BaseResponse> newPassword(@RequestParam(value = "email") String email, 
                                                    @RequestParam(value = "password") String password){
        try{
            userService.newPassword(email, password);
            return ResponseEntity.ok(new BaseResponse("success"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to update user profile password!", e));
        }
    }
    
    @PutMapping("/profile/password")
    public ResponseEntity<BaseResponse> updatePassword(@RequestParam(value = "Authorization") String token, 
                                                       @RequestBody ProfilePasswordRequest profilePasswordRequest){
        try{
            String username = jwtGenerator.getUsernameFromJWT(token);
            return userService.updatePassord(profilePasswordRequest, username);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to update user profile password!", e));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getUserProfileHandler(@RequestHeader("Authorization") String jwt) {
        try {
            return userService.getUserProfile(jwt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse("Failed to retrieve user profile.", e));
        }
    }
}
