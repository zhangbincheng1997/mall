package cn.jnu.server.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdatePwdDTO {

    @ApiModelProperty(value = "旧密码")
    @NotEmpty(message = "旧密码不能为空")
    private String oldPwd;

    @ApiModelProperty(value = "新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPwd;
}
