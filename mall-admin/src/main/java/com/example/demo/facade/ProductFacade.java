package com.example.demo.facade;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.component.redis.RedisService;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ProductFacade {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    public Product get(Long id) {
        return productService.getById(id);
    }

    public Page<Product> list(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(pageRequest.getKeyword()))
            wrapper.lambda().like(Product::getName, pageRequest.getKeyword());
        if (!StringUtils.isEmpty(pageRequest.getCategory()))
            wrapper.lambda().eq(Product::getCategory, pageRequest.getCategory());
        wrapper.lambda().orderByDesc(Product::getId);
        return productService.page(page, wrapper);
    }

    public void save(ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        productService.save(product);
        if (product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock()); // 加入预减库存
        }
    }

    public void update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto).setId(id);
        productService.updateById(product);
        if (product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock()); // 加入预减库存
        }
    }

    public void delete(Long id) {
        productService.removeById(id);
        redisService.delete(Constants.PRODUCT_STOCK + id);
    }
}
