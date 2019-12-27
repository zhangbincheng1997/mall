package com.example.demo.service;

import com.example.demo.dto.PageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService {

    Product get(Long id);

    PageInfo<Product> list(PageRequest pageRequest);

    int add(ProductDto productDto);

    int update(Long id, ProductDto productDto);

    int delete(Long id);
}
