package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService {

    Product get(Long id);

    PageInfo<Product> list(String keyword, int page, int limit);

    int add(ProductDto productDto);

    int delete(Long id);

    int update(Long id, ProductDto productDto);
}
