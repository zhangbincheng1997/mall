package cn.jnu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdRequest {

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;
}
