package com.example.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
public class GoodsVo {

    @ApiModelProperty(value = "链接")
    //    @NotNull(message = "链接不能为空")
    @URL
    private String icon;

    @ApiModelProperty(value = "标题", required = true)
//    @NotNull(message = "标题不能为空")
    @Length(min = 1, max = 255, message = "标题长度为1-255")
    private String title;

    @ApiModelProperty(value = "描述", required = true)
//    @NotNull(message = "描述不能为空")
    @Length(min = 1, max = 255, message = "描述长度为1-255")
    private String description;

    @ApiModelProperty(value = "价格", required = true, example = "0")
//    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不低于0.00")
    @DecimalMax(value = "1000000.00", message = "价格不高于1000000.00")
    private Double price;

    @ApiModelProperty(value = "库存", required = true, example = "0")
//    @NotNull(message = "库存不能为空")
    @Range(min = 0, max = 1000000, message = "库存长度为0-1000000")
    private Integer stock;
}
