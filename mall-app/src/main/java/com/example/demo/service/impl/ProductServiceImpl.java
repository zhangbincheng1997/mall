package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Product get(Long id) {
        return baseMapper.selectOne(Wrappers.<Product>lambdaQuery()
                .eq(Product::getId, id)
                .eq(Product::getStatus, true));
    }

    @Override
    public Page<Product> list(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        return baseMapper.selectPage(page,
                Wrappers.<Product>lambdaQuery()
                        .eq(Product::getStatus, true)
                        .like(Product::getName, pageRequest.getKeyword())
                        .like(Product::getCategory, pageRequest.getCategory())
                        .orderByDesc(Product::getId));
    }
}
