package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.CategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.CategoryExample;
import com.example.demo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "category") // EnableCaching
    public List<Category> list(Long id) {
        return categoryMapper.selectByExample(new CategoryExample());
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // 清除缓存
    public int create(CategoryDto categoryDto) {
        if (categoryDto.getPid() != 0L) { // 检查父类是否存在
            Category category = categoryMapper.selectByPrimaryKey(categoryDto.getPid());
            if (category == null) throw new GlobalException(Status.CATEGORY_ROOT_NOT_EXIST);
        }
        return categoryMapper.insertSelective(Convert.convert(Category.class, categoryDto));
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // 清除缓存
    public int update(Long id, CategoryDto categoryDto) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) throw new GlobalException(Status.CATEGORY_NOT_EXIST);
        category.setName(categoryDto.getName()); // name
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    // 查询条件1：a=? and (b=? or c=?) 不支持
    // 查询条件2：(a=? And b=?) or (a=? And c=?) 支持

    @Override
    @CacheEvict(value = "category", allEntries = true) // 清除缓存
    public int delete(Long id) {
        try {
            CategoryExample example = new CategoryExample();
            // 不能删除非叶子节点
            example.createCriteria().andPidEqualTo(id);
            List<Category> categoryList = categoryMapper.selectByExample(example);
            if (categoryList.size() != 0) throw new GlobalException(Status.CATEGORY_NOT_DELETE);
            return categoryMapper.deleteByPrimaryKey(id);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_EXIST); // 外键
        }
    }
}
