package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDetailVo {

    @JsonProperty(value = "name")
    private String productName;

    @JsonProperty(value = "icon")
    private String productIcon;

    @JsonProperty(value = "price")
    private String productPrice; // BigDecimal -> String

    @JsonProperty(value = "quantity")
    private Integer productQuantity;
}
