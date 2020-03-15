package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartDto {

    @ApiModelProperty(value = "商品ID", required = true, example = "0")
    @NotNull(message = "商品ID不能为空")
    private Long id;

    @ApiModelProperty(value = "商品数量", required = true, example = "0")
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "数量不低于1")
    @Max(value = 99, message = "数量不高于99")
    private Integer quantity;

    @ApiModelProperty(value = "是否选中")
    private Boolean checked = true;
}
