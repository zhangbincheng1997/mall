package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.facade.UserFacade;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;

@Api(tags = "用户")
@Validated // 单个方法参数检查
@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @ApiOperation("注册")
    @PostMapping(value = "/register")
    public Result<String> register(@Valid RegisterDto registerDto) {
        userFacade.register(registerDto);
        return Result.success();
    }

    @ApiOperation("获取信息")
    @GetMapping("/user")
    public Result<UserInfoVo> getUserInfo(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        return Result.success(Convert.convert(UserInfoVo.class, userDetails.getUser()));
    }

    @ApiOperation("修改信息")
    @PostMapping("/user")
    public Result<String> updateUserInfo(@ApiIgnore Principal principal, @Valid UserInfoDto userInfoDto) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userFacade.updateUserInfoByUsername(username, userInfoDto);
        return Result.success();
    }

    @ApiOperation("修改密码")
    @PostMapping("/user/password")
    public Result<String> updatePassword(@ApiIgnore Principal principal,
                                         @RequestParam("password")
                                         @Size(min = 3, max = 12, message = "密码长度为3-12") String password) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userFacade.updatePasswordByUsername(username, password);
        return Result.success();
    }
}
