package com.example.demo.vo;

import lombok.Data;

@Data
public class CartVo {

    private Long id; // Long -> String

    private String name;

    private String icon;

    private String price; // BigDecimal -> String

    private Integer quantity;
}
