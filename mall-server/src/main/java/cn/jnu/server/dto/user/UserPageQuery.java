package cn.jnu.server.dto.user;

import cn.jnu.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageQuery extends PageQuery {
    @ApiModelProperty(value = "用户名称")
    private String name;
    @ApiModelProperty(value = "公司名称")
    private String company;
    @ApiModelProperty(value = "部门名称")
    private String department;
    @ApiModelProperty(value = "用户角色")
    private String roleName;
    @ApiModelProperty(value = "当前状态")
    private String status;
    @ApiModelProperty(value = "开始时间（yyyy-MM-dd HH:mm:ss）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间（yyyy-MM-dd HH:mm:ss）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
