package com.example.demo.facade;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryFacade {

    @Autowired
    private CategoryService categoryService;

    @Cacheable(value = "category") // EnableCaching
    public List<Category> list(Long id) {
        return categoryService.list();
    }

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void save(CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto);
        categoryService.save(category);
    }

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void update(Long id, CategoryDto categoryDto) {
        Category category = new Category()
                .setName(categoryDto.getName())
                .setId(id);
        categoryService.updateById(category);
    }

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void delete(Long id) {
        try {
            Category category = categoryService.getOne(Wrappers.<Category>lambdaQuery()
                    .eq(Category::getPid, id));
            // 不能删除非叶子节点
            if (category != null) throw new GlobalException(Status.CATEGORY_NOT_LEAF);
            categoryService.removeById(id);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_REFERENCES); // FOREIGN KEY
        }
    }
}
