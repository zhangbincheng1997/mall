package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;

public interface ProductService extends IService<Product> {

    Product get(Long id);

    Page<Product> list(ProductPageRequest pageRequest);

    void add(ProductDto productDto);

    void update(Long id, ProductDto productDto);

    void delete(Long id);

    boolean addStock(Long id, int count);
}
