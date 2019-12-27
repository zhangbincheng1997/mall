package com.example.demo.service.impl;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.PageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.example.demo.utils.ConvertUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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
    public PageInfo<Product> list(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        String keyword = pageRequest.getKeyword();
        ProductExample example = new ProductExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andNameLike("%" + keyword + "%");
            example.or().andDescriptionLike("%" + keyword + "%");
        }
        List<Product> productList = productMapper.selectByExample(example);
        return new PageInfo<>(productList);
    }

    @Override
    public int add(ProductDto productDto) {
        try {
            return productMapper.insertSelective(ConvertUtils.convert(productDto, Product.class));
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.PRODUCT_CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public int update(Long id, ProductDto productDto) {
        Product product = new Product();
        product.setId(id);
        return productMapper.updateByPrimaryKeySelective(ConvertUtils.convert(productDto, product));
    }

    @Override
    public int delete(Long id) {
        return productMapper.deleteByPrimaryKey(id);
    }
}
