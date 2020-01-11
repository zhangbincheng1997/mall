package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.PageResult;
import com.example.demo.dto.page.ProductPageRequest;
import com.example.demo.model.Product;
import com.example.demo.service.BuyerProductService;
import com.example.demo.vo.ProductVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "买家商品")
@Controller
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private BuyerProductService buyerProductService;

    @ApiOperation("获取商品列表")
    @GetMapping("")
    @ResponseBody
    public PageResult<List<ProductVo>> list(@Valid ProductPageRequest pageRequest) {
        PageInfo<Product> pageInfo = buyerProductService.list(pageRequest);
        List<ProductVo> productVoList = pageInfo.getList().stream()
                .map(product -> Convert.convert(ProductVo.class, product))
                .collect(Collectors.toList());
        return PageResult.success(productVoList, pageInfo.getTotal());
    }
}
