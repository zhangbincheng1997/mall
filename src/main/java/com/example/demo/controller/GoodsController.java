package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.model.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品控制类")
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("获取商品")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrder(@PathVariable("id") Long id) {
        Goods goods = goodsService.get(id);
        return Result.success(goods);
    }

    @ApiOperation(value = "获取商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getList(@RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        // TODO
        List<Goods> goodsList = goodsService.getList();
        return Result.success(goodsList);
    }

    @ApiOperation(value = "添加商品")
    @RequestMapping(value = "/create")
    @ResponseBody
    public Result create(@Validated GoodsVo goodsVo) {
        int count = goodsService.create(goodsVo);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "更新商品")
    @RequestMapping(value = "/update/{id}")
    @ResponseBody
    public Result update(@PathVariable("id") Long id,
                         @Validated GoodsVo goodsVo) {
        int count = goodsService.update(id, goodsVo);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "删除商品")
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public Result delete(@RequestParam("id") Long id) {
        int count = goodsService.delete(id);
        if (count > 0) {
            return Result.success();
        } else {
            return Result.failed();
        }
    }
}
