package com.example.demo.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ProductFacade {

    @Autowired
    private ProductService productService;

    public Product get(Long id) {
        return productService.getOne(Wrappers.<Product>lambdaQuery()
                .eq(Product::getId, id)
                .eq(Product::getStatus, true));
    }

    public Page<Product> list(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(pageRequest.getKeyword()))
            wrapper.lambda().like(Product::getName, pageRequest.getKeyword());
        if (!StringUtils.isEmpty(pageRequest.getCategory()))
            wrapper.lambda().eq(Product::getCategory, pageRequest.getCategory());
        wrapper.lambda().eq(Product::getStatus, true);
        wrapper.lambda().orderByDesc(Product::getId);
        return productService.page(page, wrapper);
    }
}
