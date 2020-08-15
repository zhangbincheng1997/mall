package com.example.demo.facade;

import cn.hutool.core.convert.Convert;
import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void save(CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto);
        categoryService.save(category);
    }

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void update(Long id, CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto).setId(id);
        categoryService.updateById(category);
    }

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void delete(Long id) {
        categoryService.removeById(id);
    }
}
