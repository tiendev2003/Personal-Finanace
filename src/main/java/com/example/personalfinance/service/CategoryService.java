package com.example.personalfinance.service;

import com.example.personalfinance.bean.response.BaseResponse;
import com.example.personalfinance.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getCategoriesByUserName(String userName);

    String addCategories(Category category, String userName);

    String deleteCategories(int category_TD);

    Category getCategoryById(Integer id);

    ResponseEntity<BaseResponse> sortTransaction(Integer categoryId);
}
