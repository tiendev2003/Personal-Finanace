package com.example.personalfinance.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse {
    private Object message;
    private Object data;

    public BaseResponse(Object message) {
        this.message = message;
    }
}