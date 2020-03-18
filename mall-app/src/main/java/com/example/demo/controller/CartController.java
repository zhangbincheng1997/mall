package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.component.CartService;
import com.example.demo.dto.CartDto;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "购物车")
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @ApiOperation("获取购物车")
    @GetMapping("")
    @ResponseBody
    public Result<List<CartVo>> list(@ApiIgnore Principal principal) {
        List<CartDto> cartDtoList = cartService.list(principal.getName());
        List<CartVo> cartVoList = cartDtoList.stream()
                .map(cartDto -> {
                    Product product = productService.get(cartDto.getId()); // 获取当前商品信息
                    CartVo cartVo = Convert.convert(CartVo.class, product);
                    cartVo.setQuantity(cartDto.getQuantity());
                    cartVo.setChecked(cartDto.getChecked());
                    return cartVo;
                }).collect(Collectors.toList());
        return Result.success(cartVoList);
    }

    @ApiOperation("加入购物车")
    @PostMapping("")
    @ResponseBody
    public Result<String> add(@ApiIgnore Principal principal, @Valid CartDto cartDto) {
        cartService.add(principal.getName(), cartDto);
        return Result.success();
    }

    @ApiOperation("更新购物车")
    @PutMapping("")
    @ResponseBody
    public Result<String> update(@ApiIgnore Principal principal, @Valid CartDto cartDto) {
        cartService.update(principal.getName(), cartDto);
        return Result.success();
    }

    @ApiOperation("删除购物车")
    @DeleteMapping("/{ids}")
    @ResponseBody
    public Result<String> delete(@ApiIgnore Principal principal, @PathVariable("ids") List<Long> ids) {
        cartService.delete(principal.getName(), ids);
        return Result.success();
    }

    @ApiOperation("更新选中购物车商品")
    @PutMapping("/check/{id}")
    @ResponseBody
    public Result<String> select(@ApiIgnore Principal principal, @PathVariable("id") Long id,
                                 @RequestParam("checked") Boolean checked) {
        if (Long.valueOf(0L).equals(id)) { // id = 0 全部选中
            cartService.updateAllCheck(principal.getName(), checked);
        } else { // 否则 选中单个
            cartService.updateOneCheck(principal.getName(), id, checked);
        }
        return Result.success();
    }
}
