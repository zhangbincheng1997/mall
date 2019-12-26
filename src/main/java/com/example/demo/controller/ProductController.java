package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.dto.ProductDto;
import com.example.demo.utils.ConvertUtils;
import com.example.demo.vo.ProductVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "商品")
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('product:read')")
    public Result get(@PathVariable("id") Long id) {
        Product product = productService.get(id);
        return Result.success(ConvertUtils.convert(product, ProductVo.class));
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAuthority('product:read')")
    public Result list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo<Product> pageInfo = productService.list(keyword, page, limit);
        List<Product> productList = pageInfo.getList();
        List<ProductVo> productVoList = productList.stream()
                .map(product -> ConvertUtils.convert(product, ProductVo.class))
                .collect(Collectors.toList());
        return PageResult.success(productVoList, pageInfo.getTotal());
    }

    @ApiOperation("添加商品")
    @PutMapping("")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')") // hasAuthority('product:create')
    public Result add(@Valid ProductDto productDto) {
        int count = productService.add(productDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("修改商品")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('product:write')")
    public Result update(@PathVariable("id") Long id,
                         @Valid ProductDto productDto) {
        int count = productService.update(id, productDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('product:delete')")
    public Result delete(@PathVariable("id") Long id) {
        int count = productService.delete(id);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
