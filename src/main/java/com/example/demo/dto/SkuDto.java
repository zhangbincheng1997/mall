package com.example.demo.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDto {

    private List<JSONObject> ids;

    private BigDecimal price;

    private Integer stock;

    private String sku;
}
