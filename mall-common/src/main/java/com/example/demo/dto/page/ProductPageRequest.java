package com.example.demo.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductPageRequest extends PageRequest {

    @ApiModelProperty(value = "关键词")
    private String keyword = "";

    @ApiModelProperty(value = "分类")
    private String category = "";
}
