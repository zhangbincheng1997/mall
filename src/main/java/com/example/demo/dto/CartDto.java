package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartDto {

    @ApiModelProperty(value = "商品ID", required = true, example = "0")
    @NotNull(message = "商品ID错误")
    private Long id;

    @ApiModelProperty(value = "商品数量", required = true, example = "0")
    @NotNull(message = "商品数量错误")
    private Integer quantity;
}
