package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.component.redis.RedisService;
import com.example.demo.utils.Constants;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private RedisService redisService;

    @Override
    public Product get(Long id) {
        return baseMapper.selectById(id);
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
    public void add(ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        baseMapper.insert(product);
        if (product.getStatus() != null && product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        }
    }

    @Override
    public void update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto)
                .setId(id);
        baseMapper.updateById(product);
        if (product.getStatus() != null && product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        }
    }

    @Override
    public void delete(Long id) {
        baseMapper.deleteById(id);
        redisService.delete(Constants.PRODUCT_STOCK + id);
    }

    @Override
    public boolean addStock(Long id, int count) {
        UpdateWrapper<Product> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        wrapper.setSql("stock = stock + " + count);
        return update(wrapper);
    }
}
