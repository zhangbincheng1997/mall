package cn.jnu.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BaseVO {

    // WebConfig 已经配置 JsonFormat

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
