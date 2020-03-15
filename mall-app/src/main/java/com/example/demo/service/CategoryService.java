package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> list(Long id);

    void add(CategoryDto categoryDto);

    void update(Long id, CategoryDto categoryDto);

    void delete(Long id);
}
