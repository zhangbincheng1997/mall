package com.example.demo.service;

import com.example.demo.dto.SkuDto;
import com.example.demo.model.Attribute;
import com.example.demo.model.AttributeValue;
import com.example.demo.model.Sku;

import java.util.List;

public interface SkuService {

    List<Attribute> getAttributeName(Long categoryId);

    List<AttributeValue> getAttributeValue(Long attributeId);

    int addAttributeName(Long categoryId, String name);
    int addAttributeValue(Long attributeId, String value);
    int deleteAttributeName(Long id);
    int deleteAttributeValue(Long id);

//    int updateAttributeListToProduct(Long productId, String attributeList);

    Sku getSku(Long productId, String attribute);

    int addSku(SkuDto skuDto);

    int updateSku(Long id, SkuDto skuDto);
}
