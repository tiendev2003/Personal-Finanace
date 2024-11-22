package com.example.personalfinance.bean.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class ProfileImgRequest {
    private MultipartFile image;
}
