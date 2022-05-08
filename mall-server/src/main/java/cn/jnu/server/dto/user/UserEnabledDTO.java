package cn.jnu.server.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserEnabledDTO {

    @ApiModelProperty(value = "用户ID列表")
    @NotNull(message = "用户ID列表不能为空")
    private List<Long> userIds;

    @ApiModelProperty(value = "用户状态")
    @NotNull(message = "用户状态不能为空")
    private Integer status;
}
