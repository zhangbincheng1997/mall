package com.example.demo.facade;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryFacade {

    @Autowired
    private CategoryService categoryService;

    @Cacheable(value = "category") // EnableCaching
    public List<Category> list() {
        return categoryService.list();
    }
}
