package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class SkuDto {

    @ApiModelProperty(value = "商品ID", required = true, example = "0")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "属性", required = true)
    @Size(min = 1, max = 255, message = "属性长度为1-255")
    private String attribute;

    @ApiModelProperty(value = "价格", required = true, example = "0")
    @DecimalMin(value = "0.00", message = "价格不低于0.00")
    @DecimalMax(value = "1000000.00", message = "价格不高于1000000.00")
    private BigDecimal price = new BigDecimal(0); // 可以为空

    @ApiModelProperty(value = "库存", required = true, example = "0")
    @Min(value = 0, message = "库存不低于0")
    @Max(value = 1000000, message = "库存不高于1000000")
    private Integer stock = 0; // 可以为空
}
