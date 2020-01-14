package com.example.demo.vo;

import lombok.Data;

@Data
public class SkuVo {

    private String skuId; // SKU ID

    private String price; // BigDecimal -> String

    private Integer stock;
}
