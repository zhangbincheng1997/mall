package com.example.demo.service;

import com.example.demo.dto.PageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.model.Product;
import com.github.pagehelper.PageInfo;

public interface BuyerProductService {

    PageInfo<Product> list(PageRequest pageRequest);
}
