package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.component.RedisService;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.utils.Constants;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.security.Principal;

@Api(tags = "用户")
@Validated // 单个方法参数检查
@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("注册")
    @PostMapping(value = "/register")
    @ResponseBody
    public Result<String> register(@Valid RegisterDto registerDto) {
        int count = userService.register(registerDto);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("获取信息")
    @GetMapping("/user")
    @ResponseBody
    public Result<UserInfoVo> getUserInfo(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        return Result.success(Convert.convert(UserInfoVo.class, userDetails.getUser()));
    }

    @ApiOperation("修改信息")
    @PostMapping("/user")
    @ResponseBody
    public Result<String> updateUserInfo(@ApiIgnore Principal principal, @Valid UserInfoDto userInfoDto) {
        String username = principal.getName(); // SecurityContextHolder上下文
        int count = userService.updateUserInfoByUsername(username, userInfoDto);
        if (count != 0) {
            redisService.delete(Constants.USER_KEY + username); // 刷新缓存
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/user/password")
    @ResponseBody
    public Result<String> updatePassword(@ApiIgnore Principal principal,
                                         @RequestParam("password")
                                         @Size(min = 3, max = 12, message = "密码长度为3-12") String password) {
        String username = principal.getName(); // SecurityContextHolder上下文
        int count = userService.updatePasswordByUsername(username, password);
        if (count != 0) {
            redisService.delete(Constants.USER_KEY + username); // 刷新缓存
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
