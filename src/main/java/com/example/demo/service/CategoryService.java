package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.Category;
import com.github.pagehelper.PageInfo;

public interface CategoryService {

    Category get(Long id);

    PageInfo<Category> list(PageRequest pageRequest);

    int add(CategoryDto categoryDto);

    int update(Long id, CategoryDto categoryDto);

    int delete(Long id);
}
