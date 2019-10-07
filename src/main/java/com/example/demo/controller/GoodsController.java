package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.model.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.vo.GoodsVo;
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

    @ApiOperation(value = "获取商品数量")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:read')")
    public Result count() {
        long count = goodsService.count();
        return Result.success(count);
    }

    @ApiOperation(value = "获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:read')")
    public Result list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageInfo goodsPage = goodsService.list(keyword, pageNum, pageSize);
        return Result.success(goodsPage);
    }

    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:create')")
    public Result create(@Validated GoodsVo goodsVo) {
        int count = goodsService.create(goodsVo);
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
                         @Validated GoodsVo goodsVo) {
        int count = goodsService.update(id, goodsVo);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('goods:delete')")
    public Result delete(@PathVariable("id") Long id) {
        int count = goodsService.delete(id);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
