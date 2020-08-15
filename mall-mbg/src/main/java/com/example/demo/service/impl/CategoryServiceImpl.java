package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzz
 * @since 2020-08-15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
