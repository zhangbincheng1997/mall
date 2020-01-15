package com.example.demo.vo;

import lombok.Data;

@Data
public class SkuVo {

    // TODO

    private Long skuId; // SKU ID

    private String price; // BigDecimal -> String

    private Integer stock;
}
