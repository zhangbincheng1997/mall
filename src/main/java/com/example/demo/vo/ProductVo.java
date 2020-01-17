package com.example.demo.vo;

import lombok.Data;

@Data
public class ProductVo {

    private Long id;

    private String icon;

    private String name;

    private String description;

    private String price; // BigDecimal -> String

    private Integer stock;

    private Integer category;

    private Boolean status;

    private Boolean sku;
}
