package cn.jnu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQuery {

    @ApiModelProperty(value = "页面")
    private Integer page = 1;

    @ApiModelProperty(value = "大小")
    private Integer limit = 10;
}
