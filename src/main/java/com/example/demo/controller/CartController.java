package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.component.RedisService;
import com.example.demo.dto.CartDto;
import com.example.demo.model.Product;
import com.example.demo.service.SellerProductService;
import com.example.demo.utils.Constants;
import com.example.demo.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "购物车")
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SellerProductService productService;

    @ApiOperation("获取购物车")
    @GetMapping("")
    @ResponseBody
    public Result<List<CartVo>> list(@ApiIgnore Principal principal) {
        Set<String> keys = redisService.keys(Constants.REDIS_PRODUCT_CART + principal.getName());
        List<CartVo> cartVoList = keys.stream()
                .map(key -> (CartVo) redisService.get(key))
                .collect(Collectors.toList());
        return Result.success(cartVoList);
    }

    @ApiOperation("加入购物车")
    @PostMapping("")
    @ResponseBody
    public Result<String> create(@ApiIgnore Principal principal, CartDto cartDto) {
        Product product = productService.get(cartDto.getId());
        CartVo cartVo = Convert.convert(CartVo.class, product);
        cartVo.setQuantity(cartDto.getQuantity());
        redisService.set(Constants.REDIS_PRODUCT_CART + principal.getName() + cartVo.getId(), cartVo);
        return Result.success();
    }

    @ApiOperation("更新购物车")
    @PutMapping("")
    @ResponseBody
    public Result<String> sub(@ApiIgnore Principal principal, CartDto cartDto) {
        CartVo cartVo = (CartVo) redisService.get(Constants.REDIS_PRODUCT_CART + principal.getName() + cartDto.getId());
        cartVo.setQuantity(cartDto.getQuantity());
        return Result.success();
    }

    @ApiOperation("删除购物车")
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> delete(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        redisService.delete(Constants.REDIS_PRODUCT_CART + principal.getName() + id);
        return Result.success();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("")
    @ResponseBody
    public Result<String> clear(@ApiIgnore Principal principal) {
        Set<String> keys = redisService.keys(Constants.REDIS_PRODUCT_CART + principal.getName());
        keys.forEach(key -> redisService.delete(key));
        return Result.success();
    }
}
