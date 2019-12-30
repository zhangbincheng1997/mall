package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.PageRequest;
import com.example.demo.service.ProductService;
import com.example.demo.vo.ProductVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "买家商品")
@Controller
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("获取商品列表")
    @GetMapping("")
    @ResponseBody
    public Result<List<ProductVo>> list(@Valid PageRequest pageRequest) {
        PageInfo<ProductVo> pageInfo = productService.listByBuyer(pageRequest);
        return PageResult.success(pageInfo.getList(), pageInfo.getTotal());
    }
}
