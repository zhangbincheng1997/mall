package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> list(Long id);
}
