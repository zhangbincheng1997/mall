package com.example.demo.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SkuVo {

    private List<String> ids;

    private BigDecimal price;

    private Integer stock;

    private String sku;

    // 前端解析
    public List<JSONObject> getIds() {
        return ids.stream().map(JSON::parseObject).collect(Collectors.toList());
    }
}
