package cn.jnu.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IdsRequest {

    @ApiModelProperty(value = "IDs")
    @NotNull(message = "IDs不能为空")
    private List<Long> ids;
}
