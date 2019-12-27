package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.CategoryExample;
import com.example.demo.service.CategoryService;
import com.example.demo.utils.ConvertUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "category") // EnableCaching
    public Category get(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Category> list(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        String keyword = pageRequest.getKeyword();
        CategoryExample example = new CategoryExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andNameLike("%" + keyword + "%");
        }
        List<Category> categoryList = categoryMapper.selectByExample(example);
        return new PageInfo<>(categoryList);
    }

    @Override
    public int add(CategoryDto categoryDto) {
        return categoryMapper.updateByPrimaryKeySelective(ConvertUtils.convert(categoryDto, Category.class));
    }

    @Override
    public int update(Long id, CategoryDto categoryDto) {
        try {
            Category category = new Category();
            category.setId(id);
            return categoryMapper.updateByPrimaryKeySelective(ConvertUtils.convert(categoryDto, category));
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.PRODUCT_CATEGORY_EXIST);
        }
    }

    @Override
    public int delete(Long id) {
        try {
            return categoryMapper.deleteByPrimaryKey(id);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.PRODUCT_CATEGORY_EXIST);
        }
    }
}
