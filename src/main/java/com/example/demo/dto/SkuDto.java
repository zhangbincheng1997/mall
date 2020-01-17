package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuDto {

    private String ids;

    private BigDecimal price;

    private Integer stock;

    private String sku;
}
