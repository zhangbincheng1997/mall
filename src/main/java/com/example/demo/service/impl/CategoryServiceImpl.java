package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.CategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.model.CategoryExample;
import com.example.demo.service.CategoryService;
import com.example.demo.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "category") // EnableCaching
    public List<CategoryVo> list(Long id) {
        // 全部读出 加速搜索
        CategoryExample example = new CategoryExample();
        List<Category> categoryList = categoryMapper.selectByExample(example);
        return get(categoryList, id);
    }

    // 递归调用
    public List<CategoryVo> get(List<Category> categoryList, Long id) {
        List<CategoryVo> categoryVoList = getByPid(categoryList, id);
        categoryVoList.forEach(categoryVo -> categoryVo.setChildren(get(categoryList, categoryVo.getId())));
        return categoryVoList;
    }

    public List<CategoryVo> getByPid(List<Category> categoryList, Long id) {
        return categoryList.stream()
                .filter(category -> category.getPid().equals(id))
                .map(category -> Convert.convert(CategoryVo.class, category))
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "category", allEntries = true) // 清除缓存
    public int add(CategoryDto categoryDto) {
        Category category = categoryMapper.selectByPrimaryKey(categoryDto.getPid());
        if (category == null) throw new GlobalException(Status.CATEGORY_EXIST);
        category.setName(categoryDto.getName()); // name
        category.setPid(categoryDto.getPid()); // pid
        return categoryMapper.insertSelective(category);
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
