package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.model.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.dto.GoodsDto;
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
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("获取商品")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:read')")
    public Result get(@PathVariable("id") Long id) {
        Goods goods = goodsService.get(id);
        return Result.success(goods);
    }

    @ApiOperation(value = "获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:read')")
    public Result list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo pageInfo = goodsService.list(keyword, page, limit);
        return PageResult.success(pageInfo.getList(), pageInfo.getTotal());
    }

    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:save')")
    public Result save(@Validated GoodsDto goodsDto) {
        int count = goodsService.save(goodsDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:update')")
    public Result update(@PathVariable("id") Long id,
                         @Validated GoodsDto goodsDto) {
        int count = goodsService.update(id, goodsDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:remove')")
    public Result remove(@PathVariable("id") Long id) {
        int count = goodsService.remove(id);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
