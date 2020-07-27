package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    @Cacheable(value = "category") // EnableCaching
    public List<Category> list(Long id) {
        return list();
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void add(CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto);
        baseMapper.insert(category);
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void update(Long id, CategoryDto categoryDto) {
        Category category = new Category()
                .setName(categoryDto.getName())
                .setId(id);
        baseMapper.updateById(category);
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void delete(Long id) {
        try {
            Category category = baseMapper.selectOne(Wrappers.<Category>lambdaQuery()
                    .eq(Category::getPid, id));
            // 不能删除非叶子节点
            if (category != null) throw new GlobalException(Status.CATEGORY_NOT_LEAF);
            baseMapper.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_REFERENCES); // FOREIGN KEY
        }
    }
}
