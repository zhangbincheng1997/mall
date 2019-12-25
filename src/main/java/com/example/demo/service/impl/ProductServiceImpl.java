package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Cacheable(value = "product") // EnableCaching
    public Product get(Long id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> list(String keyword, int page, int limit) {
        PageHelper.startPage(page, limit);
        ProductExample example = new ProductExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andNameLike("%" + keyword + "%");
            example.or().andDescriptionLike("%" + keyword + "%");
        }
        List<Product> productList = productMapper.selectByExample(example);
        PageInfo<Product> pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    @Override
    public int add(ProductDto productDto) {
        Product product = new Product();
        BeanUtil.copyProperties(productDto, product);
        product.setCategoryId(1); // TODO
        return productMapper.insertSelective(product);
    }

    @Override
    public int delete(Long id) {
        return productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Long id, ProductDto productDto) {
        Product product = new Product();
        product.setId(id);
        BeanUtil.copyProperties(productDto, product);
        return productMapper.updateByPrimaryKeySelective(product);
    }
}
