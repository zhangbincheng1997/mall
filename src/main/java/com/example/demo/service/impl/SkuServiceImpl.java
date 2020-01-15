package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.SkuDto;
import com.example.demo.entity.Attribute;
import com.example.demo.entity.AttributeValue;
import com.example.demo.entity.Sku;
import com.example.demo.mapper.SkuMapper;
import com.example.demo.service.AttributeService;
import com.example.demo.service.AttributeValueService;
import com.example.demo.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private AttributeValueService attributeValueService;

    @Override
    public List<Attribute> getAttributeName(Long categoryId) {
        return attributeService.list(Wrappers.<Attribute>lambdaQuery()
                .eq(Attribute::getCategoryId, categoryId));
    }

    @Override
    public List<AttributeValue> getAttributeValue(Long attributeId) {
        return attributeValueService.list(Wrappers.<AttributeValue>lambdaQuery()
                .eq(AttributeValue::getAttributeId, attributeId));
    }

    @Override
    public Attribute addAttributeName(Long categoryId, String name) {
        Attribute attribute = new Attribute()
                .setCategoryId(categoryId)
                .setName(name);
        attributeService.save(attribute);
        return attribute;
    }

    @Override
    public AttributeValue addAttributeValue(Long attributeId, String value) {
        AttributeValue attributeValue = new AttributeValue()
                .setAttributeId(attributeId)
                .setValue(value);
        attributeValueService.save(attributeValue);
        return attributeValue;
    }

    // TODO 怎么保证完全相关删除SKU 更新product attribute list
    @Override
    public void deleteAttributeName(Long id) {
        attributeService.removeById(id);
    }

    // TODO 怎么保证完全相关删除SKU 更新product attribute list
    @Override
    public void deleteAttributeValue(Long id) {
        attributeValueService.removeById(id);
    }

    @Override
    public Sku getSku(Long productId, String attribute) {
        return baseMapper.selectOne(Wrappers.<Sku>lambdaQuery()
                .eq(Sku::getProductId, productId)
                .eq(Sku::getAttribute, attribute));
    }

    @Override
    public int addSku(SkuDto skuDto) {
        Sku sku = Convert.convert(Sku.class, skuDto);
        return baseMapper.insert(sku);
    }

    @Override
    public int updateSku(Long skuId, SkuDto skuDto) {
        Sku sku = Convert.convert(Sku.class, skuDto)
                .setId(skuId);
        return baseMapper.updateById(sku);
    }
}
