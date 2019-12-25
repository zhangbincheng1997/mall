package com.example.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.demo.base.Result;
import com.example.demo.component.RedisService;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserDto;
import com.example.demo.utils.Constants;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(tags = "用户控制类")
@Slf4j
@Validated
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("注册")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@Validated UserDto userDto) {
        User user = userService.register(userDto);
        return Result.success(user);
    }

    @ApiOperation("获取信息")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserInfo(Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userDetails.getUser(), userInfoVo, CopyOptions.create().setIgnoreNullValue(true));
        return Result.success(userInfoVo);
    }

    @ApiOperation("修改信息")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserInfo(Principal principal, @Validated UserInfoDto userInfoDto) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userService.updateUserInfoByUsername(username, userInfoDto);
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
        return Result.success();
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/user/password", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(Principal principal, @RequestParam("password") String password) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userService.updatePasswordByUsername(username, password);
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
        return Result.success();
    }
}
