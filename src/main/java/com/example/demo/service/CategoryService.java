package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> list(Long id);

    int create(CategoryDto categoryDto);

    int update(Long id, CategoryDto categoryDto);

    int delete(Long id);
}
