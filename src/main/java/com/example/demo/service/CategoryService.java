package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    List<CategoryVo> list(Long id);

    int add(CategoryDto categoryDto);

    int update(Long id, CategoryDto categoryDto);

    int delete(Long id);
}
