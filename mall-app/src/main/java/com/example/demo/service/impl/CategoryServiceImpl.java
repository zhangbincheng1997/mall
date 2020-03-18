package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    @Cacheable(value = "category") // EnableCaching
    public List<Category> list(Long id) {
        return list();
    }
}
