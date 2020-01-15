package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.common.base.Result;
import com.example.demo.entity.Attribute;
import com.example.demo.entity.AttributeValue;
import com.example.demo.service.SkuService;
import com.example.demo.vo.AttributeValueVo;
import com.example.demo.vo.AttributeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "商品SKU")
@Controller
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @ApiOperation("根据分类ID获取属性")
    @GetMapping("/{id}")
    @ResponseBody
    public Result<List<AttributeVo>> list(@PathVariable("id") Long id) {
        List<Attribute> attributeList = skuService.getAttributeName(id);

        List<AttributeVo> attributeVoList = attributeList.stream().map(attr -> {
            AttributeVo attributeVo = Convert.convert(AttributeVo.class, attr);
            List<AttributeValue> attributeValueList = skuService.getAttributeValue(attr.getId());
            List<AttributeValueVo> sub = attributeValueList.stream()
                    .map(value -> Convert.convert(AttributeValueVo.class, value))
                    .collect(Collectors.toList());
            attributeVo.setSub(sub);

            return attributeVo;
        }).collect(Collectors.toList());
        return Result.success(attributeVoList);
    }

    @ApiOperation("添加属性名")
    @PostMapping("/name/{id}")
    @ResponseBody
    public Result<AttributeVo> addName(@PathVariable("id") Long id, String name) {
        Attribute attribute = skuService.addAttributeName(id, name);
        return Result.success(Convert.convert(AttributeVo.class, attribute));
    }

    @ApiOperation("添加属性值")
    @PostMapping("/value/{id}")
    @ResponseBody
    public Result<AttributeValueVo> addValue(@PathVariable("id") Long id, String value) {
        AttributeValue attributeValue = skuService.addAttributeValue(id, value);
        return Result.success(Convert.convert(AttributeValueVo.class, attributeValue));
    }

    @ApiOperation("删除属性名")
    @DeleteMapping("/name/{id}")
    @ResponseBody
    public Result<String> deleteName(@PathVariable("id") Long id) {
        skuService.deleteAttributeName(id);
        return Result.success();
    }

    @ApiOperation("删除属性值")
    @DeleteMapping("/value/{id}")
    @ResponseBody
    public Result<String> deleteValue(@PathVariable("id") Long id) {
        skuService.deleteAttributeValue(id);
        return Result.success();
    }
}
