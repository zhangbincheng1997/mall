package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductDto {

    @ApiModelProperty(value = "图标", required = true)
    @NotEmpty(message = "图标不能为空")
    @URL(message = "图标不符合规范")
    private String icon;

    @ApiModelProperty(value = "名称", required = true)
    @Size(min = 1, max = 255, message = "名称长度为1-255")
    private String name;

    @ApiModelProperty(value = "描述", required = true)
    @Size(min = 1, max = 255, message = "描述长度为1-255")
    private String description;

    @ApiModelProperty(value = "价格", required = true, example = "0")
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不低于0.00")
    @DecimalMax(value = "1000000.00", message = "价格不高于1000000.00")
    private BigDecimal price;

    @ApiModelProperty(value = "库存", required = true, example = "0")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不低于0")
    @Max(value = 1000000, message = "库存不高于1000000")
    private Integer stock;

    @ApiModelProperty(value = "分类", required = true, example = "0")
    @NotNull(message = "分类不能为空")
    private Long category;

    @ApiModelProperty(value = "状态", required = true, example = "0")
    private Boolean status;
}
