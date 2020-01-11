package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.SellerProductService;
import com.example.demo.utils.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SellerProductServiceImpl implements SellerProductService {

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
    public PageInfo<Product> list(ProductPageRequest pageRequest) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();

        String keyword = pageRequest.getKeyword();
        String select = pageRequest.getCategory();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(select)) {
            criteria.andCategoryEqualTo(new Long(select));
        }

        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "id desc");
        List<Product> productList = productMapper.selectByExample(example);
        return new PageInfo<>(productList);
    }

    @Override
    public int create(ProductDto productDto) {
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

    private void addToRedis(Product product) {
        if (!product.getStatus()) return;
        redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + product.getId(), Constants.REDIS_LOCK_LEASE_TIME);
        redisService.set(Constants.REDIS_PRODUCT_STOCK + product.getId(), product.getStock());
        redisLocker.unlock(Constants.REDIS_PRODUCT_REDIS_LOCK + product.getId());
    }

    private void deleteFromRedis(Long id) {
        redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + id, Constants.REDIS_LOCK_LEASE_TIME);
        redisService.delete(Constants.REDIS_PRODUCT_STOCK + id);
        redisLocker.unlock(Constants.REDIS_PRODUCT_REDIS_LOCK + id);
    }
}
