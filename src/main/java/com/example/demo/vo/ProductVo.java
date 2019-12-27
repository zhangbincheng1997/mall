package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductVo {

    private long id;

    private String icon;

    private String name;

    private String description;

    private String price; // Product: BigDecimal -> ProductVo: String

    private Integer stock;

    @JsonProperty("category_id")
    private Integer categoryId;

    private Boolean status;
}
