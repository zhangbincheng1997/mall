package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderDetailDto {

    @NotNull(message = "商品ID错误")
    private Long id;

    @NotNull(message = "商品数量错误")
    private Integer quantity;
}
