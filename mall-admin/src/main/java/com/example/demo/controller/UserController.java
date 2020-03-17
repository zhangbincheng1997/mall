package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "用户")
@Validated // 单个方法参数检查
@Controller
@RequestMapping("")
public class UserController {

    @ApiOperation("获取信息")
    @GetMapping("/user")
    @ResponseBody
    public Result<UserInfoVo> getUserInfo(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        return Result.success(Convert.convert(UserInfoVo.class, userDetails.getUser()));
    }
}
