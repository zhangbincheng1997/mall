package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.SkuDto;
import com.example.demo.mapper.AttributeMapper;
import com.example.demo.mapper.AttributeValueMapper;
import com.example.demo.mapper.SkuMapper;
import com.example.demo.model.*;
import com.example.demo.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private AttributeValueMapper attributeValueMapper;

    @Override
    public List<Attribute> getAttributeName(Long categoryId) {
        AttributeExample example = new AttributeExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        return attributeMapper.selectByExample(example);
    }

    @Override
    public List<AttributeValue> getAttributeValue(Long attributeId) {
        AttributeValueExample example = new AttributeValueExample();
        example.createCriteria().andAttributeIdEqualTo(attributeId);
        return attributeValueMapper.selectByExample(example);
    }

    @Override
    public int addAttributeName(Long categoryId, String name) {
        Attribute attribute = new Attribute();
        attribute.setCategoryId(categoryId);
        attribute.setName(name);
        return attributeMapper.insert(attribute);
    }

    @Override
    public int addAttributeValue(Long attributeId, String value) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttributeId(attributeId);
        attributeValue.setValue(value);
        return attributeValueMapper.insert(attributeValue);
    }

    // TODO 怎么保证完全相关删除SKU 更新product attribute list
    @Override
    public int deleteAttributeName(Long id) {
        return attributeMapper.deleteByPrimaryKey(id);
    }

    // TODO 怎么保证完全相关删除SKU 更新product attribute list
    @Override
    public int deleteAttributeValue(Long id) {
        return attributeValueMapper.deleteByPrimaryKey(id);
    }

//    @Override
//    public int updateAttributeListToProduct(Long productId, String attributeList) {
//        return 0;
//    }

    @Override
    public Sku getSku(Long productId, String attribute) {
        SkuExample example = new SkuExample();
        example.createCriteria().andProductIdEqualTo(productId).andAttributeEqualTo(attribute);
        List<Sku> skuList = skuMapper.selectByExample(example);
        if (skuList.size() <= 0) throw new GlobalException(Status.SKU_NOT_EXIST);
        return skuList.get(0);
    }

    @Override
    public int addSku(SkuDto skuDto) {
        Sku sku = Convert.convert(Sku.class, skuDto);
        return skuMapper.insertSelective(sku);
    }

    @Override
    public int updateSku(Long skuId, SkuDto skuDto) {
        Sku sku = Convert.convert(Sku.class, skuDto);
        sku.setId(skuId);
        return skuMapper.updateByPrimaryKeySelective(sku);
    }
}
