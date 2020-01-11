package com.example.demo.service;

import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.github.pagehelper.PageInfo;

public interface SellerProductService {

    Product get(Long id);

    PageInfo<Product> list(ProductPageRequest pageRequest);

    int create(ProductDto productDto);

    int update(Long id, ProductDto productDto);

    int delete(Long id);
}
