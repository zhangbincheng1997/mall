package cn.jnu.server.controller;

import cn.jnu.common.base.Result;
import cn.jnu.server.compoment.RequestService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Api(tags = "认证")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RequestService requestService;

    /**
     * 实现转发登录接口
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody @Valid LoginReq req) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/login")
                .queryParam("username", req.getUsername())
                .queryParam("password", req.getPassword());
        String api = builder.toUriString();
        return requestService.post(api, new JSONObject(), new ParameterizedTypeReference<Result<Object>>() {
        });
    }

    @Data
    public static class LoginReq {
        @ApiModelProperty(value = "账号")
        @NotEmpty(message = "账号不能为空")
        @Length(min = 3, max = 12, message = "账号长度为3-12")
        private String username;

        @ApiModelProperty(value = "密码")
        @NotEmpty(message = "密码不能为空")
        @Length(min = 3, max = 12, message = "密码长度为3-12")
        private String password;
    }
}
