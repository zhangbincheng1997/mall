package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.entity.Product;
import com.example.demo.facade.ProductFacade;
import com.example.demo.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "商品")
@RestController
@RequestMapping("/product")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProductController {

    @Autowired
    private ProductFacade productFacade;

    @ApiOperation("获取商品")
    @GetMapping("/{id}")
    public Result<ProductVo> get(@PathVariable("id") Long id) {
        Product product = productFacade.get(id);
        return Result.success(Convert.convert(ProductVo.class, product));
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    public PageResult<List<ProductVo>> list(@Valid ProductPageRequest pageRequest) {
        Page<Product> page = productFacade.list(pageRequest);
        List<ProductVo> productVoList = Convert.convert(new TypeReference<List<ProductVo>>() {
        }, page.getRecords());
        return PageResult.success(productVoList, page.getTotal());
    }

    @ApiOperation("添加商品")
    @PostMapping("")
    public Result<Long> save(@Valid ProductDto productDto) {
        productFacade.save(productDto);
        return Result.success();
    }

    @ApiOperation("修改商品")
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable("id") Long id, @Valid ProductDto productDto) {
        productFacade.update(id, productDto);
        return Result.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        productFacade.delete(id);
        return Result.success();
    }
}
