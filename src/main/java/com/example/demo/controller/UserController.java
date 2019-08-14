package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.access.AccessLimit;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.vo.LoginVo;
import com.example.demo.vo.UpdatePassVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Api(tags = "用户控制类")
public class UserController {

    @Resource
    UserService userService;

    @ApiOperation("注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
    })
    @PostMapping("/register")
    @ResponseBody
    public Result register(@Valid LoginVo loginVo) {
        return userService.register(loginVo);
    }

    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Result login(HttpServletResponse response, @Valid LoginVo loginVo) {
        return userService.login(response, loginVo);
    }

    @ApiOperation("注销")
    @GetMapping("/logout")
    @ResponseBody
    @AccessLimit
    public Result logout(HttpServletResponse response, User user) {
        return userService.logout(response, user);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    @ResponseBody
    @AccessLimit
    public Result getUserInfo(User user) {
        return userService.getUserInfo(user);
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPass", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPass", value = "新密码", required = true, dataType = "String"),
    })
    @PostMapping("/updatePass")
    @ResponseBody
    @AccessLimit
    public Result updatePass(User user, @Valid UpdatePassVo updatePassVo) {
        return userService.updatePass(user, updatePassVo);
    }
}
