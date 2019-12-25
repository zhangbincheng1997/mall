package com.example.demo.service.impl;

import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper goodsMapper;

    @Override
    @Cacheable(value = "goods") // EnableCaching
    public Product get(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> list(String keyword, int page, int limit) {
        PageHelper.startPage(page, limit);
        ProductExample example = new ProductExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.or().andNameLike("%" + keyword + "%");
            example.or().andDescriptionLike("%" + keyword + "%");
        }
        List<Product> goodsList = goodsMapper.selectByExample(example);
        PageInfo<Product> pageInfo = new PageInfo(goodsList);
        return pageInfo;
    }

    @Override
    public int save(ProductDto productDto) {
        Product goods = new Product();
        BeanUtils.copyProperties(productDto, goods);
        return goodsMapper.insert(goods);
    }

    @Override
    public int remove(Long id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Long id, ProductDto productDto) {
        Product goods = new Product();
        goods.setId(id);
        BeanUtils.copyProperties(productDto, goods);
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }
}
