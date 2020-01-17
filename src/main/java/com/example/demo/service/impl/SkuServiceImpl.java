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
import java.util.stream.Collectors;

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
    public List<Sku> getAllSku(Long productId) {
        return baseMapper.selectList(Wrappers.<Sku>lambdaQuery()
                .eq(Sku::getProductId, productId));
    }

    @Override
    public Sku getSku(Long productId, String attribute) {
        return baseMapper.selectOne(Wrappers.<Sku>lambdaQuery()
                .eq(Sku::getProductId, productId)
                .eq(Sku::getIds, attribute));
    }

    // TODO 这里起名已经混乱，未来修改
    @Override
    public Sku getBySku(Long productId, String sku) {
        return baseMapper.selectOne(Wrappers.<Sku>lambdaQuery()
                .eq(Sku::getProductId, productId)
                .eq(Sku::getSku, sku));
    }

    @Override
    public void saveOrUpdateSku(Long productId, List<SkuDto> skuDtoList) {
        // 删除原有的
        baseMapper.delete(Wrappers.<Sku>lambdaUpdate()
                .eq(Sku::getProductId, productId));

        skuDtoList.forEach(skuDto -> {
            Sku sku = Convert.convert(Sku.class, skuDto).setProductId(productId);
            // 如果数据库存在相同的productId和Ids，则更新
            // 否则添加
            saveOrUpdate(sku, Wrappers.<Sku>lambdaUpdate()
                    .eq(Sku::getProductId, productId)
                    .eq(Sku::getIds, sku.getIds()));
        });
    }
}
