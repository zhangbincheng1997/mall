package com.example.demo.service;

import com.example.demo.dto.SkuDto;
import com.example.demo.entity.Attribute;
import com.example.demo.entity.AttributeValue;
import com.example.demo.entity.Sku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SkuService extends IService<Sku> {

    List<Attribute> getAttributeName(Long categoryId);

    List<AttributeValue> getAttributeValue(Long attributeId);

    Attribute addAttributeName(Long categoryId, String name);

    AttributeValue addAttributeValue(Long attributeId, String value);

    void deleteAttributeName(Long id);

    void deleteAttributeValue(Long id);

    List<Sku> getAllSku(Long productId);

    Sku getSku(Long productId, String attribute);

    void saveOrUpdateSku(Long productId, List<SkuDto> skuDtoList);
}
