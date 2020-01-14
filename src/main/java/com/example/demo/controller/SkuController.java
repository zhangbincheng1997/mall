package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.model.Attribute;
import com.example.demo.model.AttributeValue;
import com.example.demo.service.SkuService;
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
            List<AttributeVo.AttributeValueVo> sub = attributeValueList.stream()
                    .map(value -> Convert.convert(AttributeVo.AttributeValueVo.class, value))
                    .collect(Collectors.toList());
            attributeVo.setSub(sub);

            return attributeVo;
        }).collect(Collectors.toList());
        return Result.success(attributeVoList);
    }
}
