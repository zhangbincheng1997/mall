package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.PageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        ProductExample example = new ProductExample();
        return list(example, pageRequest);
    }

    @Override
    public int add(ProductDto productDto) {
        try {
            return productMapper.insertSelective(Convert.convert(Product.class, productDto));
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public int update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        product.setId(id);
        return productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public int delete(Long id) {
        return productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> listByBuyer(PageRequest pageRequest) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(true); // 上架状态
        criteria.andStockGreaterThan(0); // 有库存
        return list(example, pageRequest);
    }

    private PageInfo<Product> list(ProductExample example, PageRequest pageRequest) {
        String keyword = pageRequest.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            example.getOredCriteria().get(0).andNameLike("%" + keyword + "%");
        }
        String category = pageRequest.getCategory();
        if (!StringUtils.isEmpty(category)) {
            example.getOredCriteria().get(0).andCategoryEqualTo(new Long(category));
        }
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        List<Product> productList = productMapper.selectByExample(example);
        return new PageInfo<>(productList);
    }
}
