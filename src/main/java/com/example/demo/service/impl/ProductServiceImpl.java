package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.dto.PageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product get(Long id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Product> list(PageRequest pageRequest) {
        ProductExample example = new ProductExample();
        return list(example, pageRequest);
    }

    private void addToRedis(Product product) {
        if (!product.getStatus()) return;
        redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + product.getId(), Constants.REDIS_LOCK_LEASE_TIME);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("icon", product.getIcon());
        jsonObject.put("name", product.getName());
        jsonObject.put("price", product.getPrice());
        redisService.set(Constants.REDIS_PRODUCT_INFO + product.getId(), jsonObject);
        redisService.set(Constants.REDIS_PRODUCT_STOCK + product.getId(), product.getStock());
        redisLocker.unlock(Constants.REDIS_PRODUCT_REDIS_LOCK + product.getId());
    }

    private void deleteFromRedis(Long id) {
        redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + id, Constants.REDIS_LOCK_LEASE_TIME);
        redisService.delete(Constants.REDIS_PRODUCT_STOCK + id);
        redisService.delete(Constants.REDIS_PRODUCT_INFO + id);
        redisLocker.unlock(Constants.REDIS_PRODUCT_REDIS_LOCK + id);
    }

    @Override
    public int add(ProductDto productDto) {
        try {
            Product product = Convert.convert(Product.class, productDto);
            int count = productMapper.insertSelective(product);
            if (count != 0) addToRedis(product);
            return count;
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public int update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        product.setId(id);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count != 0) addToRedis(product);
        return count;
    }

    @Override
    public int delete(Long id) {
        int count = productMapper.deleteByPrimaryKey(id);
        if (count != 0) deleteFromRedis(id);
        return count;
    }

    @Override
    public PageInfo<Product> listByBuyer(PageRequest pageRequest) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(true); // 上架状态
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
