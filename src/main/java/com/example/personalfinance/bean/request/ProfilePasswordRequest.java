package com.example.personalfinance.bean.request;

import lombok.Data;

@Data
public class ProfilePasswordRequest {
    private String newPassword;
    private String oldPassword;
}
