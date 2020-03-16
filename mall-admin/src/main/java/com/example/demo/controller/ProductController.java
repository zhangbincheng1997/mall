package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "卖家商品")
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<ProductVo> get(@PathVariable("id") Long id) {
        Product product = productService.get(id);
        return Result.success(Convert.convert(ProductVo.class, product));
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult<List<ProductVo>> list(@Valid ProductPageRequest pageRequest) {
        Page<Product> page = productService.list(pageRequest);
        List<ProductVo> productVoList = page.getRecords().stream()
                .map(product -> Convert.convert(ProductVo.class, product))
                .collect(Collectors.toList());
        return PageResult.success(productVoList, page.getTotal());
    }

    @ApiOperation("添加商品")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Long> add(@Valid ProductDto productDto) {
        Long productId = productService.add(productDto);
        return Result.success(productId);
    }

    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<String> update(@PathVariable("id") Long id,
                                 @Valid ProductDto productDto) {
        productService.update(id, productDto);
        return Result.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<String> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return Result.success();
    }
}
