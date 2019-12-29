package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.Category;
import com.example.demo.vo.CategoryVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {

    List<CategoryVo> list(Long id);

    int add(CategoryDto categoryDto);

    int update(Long id, CategoryDto categoryDto);

    int delete(Long id);
}
