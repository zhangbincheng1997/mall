package com.example.demo.service;

import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.model.Product;
import com.github.pagehelper.PageInfo;

public interface BuyerProductService {

    PageInfo<Product> list(ProductPageRequest pageRequest);
}
