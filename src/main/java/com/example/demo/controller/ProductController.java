package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.dto.ProductDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "商品控制类")
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('product:read')")
    public Result get(@PathVariable("id") Long id) {
        Product goods = productService.get(id);
        return Result.success(goods);
    }

    @ApiOperation(value = "获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('product:read')")
    public Result list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo pageInfo = productService.list(keyword, page, limit);
        return PageResult.success(pageInfo.getList(), pageInfo.getTotal());
    }

    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('product:save')")
    public Result save(@Validated ProductDto productDto) {
        int count = productService.save(productDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('product:update')")
    public Result update(@PathVariable("id") Long id,
                         @Validated ProductDto productDto) {
        int count = productService.update(id, productDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('product:remove')")
    public Result remove(@PathVariable("id") Long id) {
        int count = productService.remove(id);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
