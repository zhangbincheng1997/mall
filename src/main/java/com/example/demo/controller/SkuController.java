package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.base.Result;
import com.example.demo.dto.SkuDto;
import com.example.demo.entity.Attribute;
import com.example.demo.entity.AttributeValue;
import com.example.demo.entity.Sku;
import com.example.demo.service.SkuService;
import com.example.demo.vo.AttributeValueVo;
import com.example.demo.vo.AttributeVo;
import com.example.demo.vo.OrderVo;
import com.example.demo.vo.SkuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "商品SKU")
@Controller
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @ApiOperation("根据分类ID获取属性")
    @GetMapping("/attr/{id}")
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

    @ApiOperation("返回SKU对应信息")
    @GetMapping("/{id}")
    @ResponseBody
    public Result<SkuVo> getSku(@PathVariable("id") Long id, @RequestParam String attribute) {
        Sku sku = skuService.getSku(id, attribute);
        SkuVo skuVo = Convert.convert(SkuVo.class, sku);
        return Result.success(skuVo);
    }

    @ApiOperation("返回SKU列表")
    @GetMapping("/product/{id}")
    @ResponseBody
    public Result<List<SkuVo>> listSku(@PathVariable("id") Long id) {
        List<Sku> skuList = skuService.getAllSku(id);
        List<SkuVo> skuVoList = Convert.convert(new TypeReference<List<SkuVo>>() {
        }, skuList);
        return Result.success(skuVoList);
    }

    @ApiOperation("添加或修改属性值")
    @PostMapping("/product/{id}")
    @ResponseBody
    public Result<String> addSku(@PathVariable("id") Long id, @RequestParam String json) {
        System.out.println(json);
        List<SkuDto> skuList = JSONObject.parseArray(json, SkuDto.class);
        System.out.println(skuList.get(0).getIds());
        skuService.saveOrUpdateSku(id, skuList);
        return Result.success();
    }
}
