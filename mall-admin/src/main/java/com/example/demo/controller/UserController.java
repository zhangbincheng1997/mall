package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "用户")
@RestController
@RequestMapping("")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    @ApiOperation("获取信息")
    @GetMapping("/user")
    public Result<UserInfoVo> getUserInfo(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        UserInfoVo userInfoVo = Convert.convert(UserInfoVo.class, userDetails.getUser());
        return Result.success(userInfoVo);
    }
}
