package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.common.base.GlobalException;
import com.example.demo.common.base.Status;
import com.example.demo.dto.SkuDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sku;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import com.example.demo.common.utils.Constants;
import com.example.demo.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private SkuService skuService;

    @Override
    public Product get(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Product getBuyer(Long id) {
        return baseMapper.selectOne(Wrappers.<Product>lambdaQuery()
                .eq(Product::getId, id)
                .eq(Product::getStatus, true));
    }

    @Override
    public Page<Product> list(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        return baseMapper.selectPage(page,
                Wrappers.<Product>lambdaQuery()
                        .like(Product::getName, pageRequest.getKeyword())
                        .like(Product::getCategory, pageRequest.getCategory())
                        .orderByDesc(Product::getId));
    }

    @Override
    public Page<Product> listBuyer(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        return baseMapper.selectPage(page,
                Wrappers.<Product>lambdaQuery()
                        .eq(Product::getStatus, true)
                        .like(Product::getName, pageRequest.getKeyword())
                        .like(Product::getCategory, pageRequest.getCategory())
                        .orderByDesc(Product::getId));
    }

    @Override
    public Long add(ProductDto productDto) {
        try {
            Product product = Convert.convert(Product.class, productDto);
            baseMapper.insert(product);
            addToRedis(product);
            return product.getId();
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(Status.CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public void update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto)
                .setId(id);
        baseMapper.updateById(product);
        addToRedis(product);
    }

    @Override
    public void delete(Long id) {
        baseMapper.deleteById(id);
        deleteFromRedis(id);
    }

    private void addToRedis(Product product) {
        if (product.getStatus() != null && !product.getStatus()) return;
        redisLocker.lock(Constants.PRODUCT_REDIS_LOCK + product.getId());
        redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        redisLocker.unlock(Constants.PRODUCT_REDIS_LOCK + product.getId());
    }

    private void deleteFromRedis(Long id) {
        redisLocker.lock(Constants.PRODUCT_REDIS_LOCK + id);
        redisService.delete(Constants.PRODUCT_STOCK + id);
        redisLocker.unlock(Constants.PRODUCT_REDIS_LOCK + id);
    }
}
