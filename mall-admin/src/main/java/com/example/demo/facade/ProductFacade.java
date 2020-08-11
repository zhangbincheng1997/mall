package com.example.demo.facade;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
        return productService.page(page,
                Wrappers.<Product>lambdaQuery()
                        .like(Product::getName, pageRequest.getKeyword())
                        .like(Product::getCategory, pageRequest.getCategory())
                        .orderByDesc(Product::getId));
    }

    public void save(ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        productService.save(product);
        if (product.getStatus() != null && product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        }
    }

    public void update(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto)
                .setId(id);
        productService.updateById(product);
        if (product.getStatus() != null && product.getStatus()) {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        }
    }

    public void delete(Long id) {
        productService.removeById(id);
        redisService.delete(Constants.PRODUCT_STOCK + id);
    }
}
