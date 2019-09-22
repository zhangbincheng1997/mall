package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.access.AccessLimit;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.vo.InfoVo;
import com.example.demo.vo.UserVo;
import com.example.demo.vo.UpdatePassVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @RequestMapping("/register")
    @ResponseBody
    public Result register(@Validated UserVo loginVo) {
        return userService.register(loginVo);
    }

    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/login")
    @ResponseBody
    public Result login(HttpServletResponse response, @Validated UserVo loginVo) {
        return userService.login(response, loginVo);
    }

    @ApiOperation("注销")
    @RequestMapping("/logout")
    @ResponseBody
    @AccessLimit
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/getUserInfo")
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
    @RequestMapping("/updatePass")
    @ResponseBody
    public Result updatePass(HttpServletRequest request, HttpServletResponse response, @Validated UpdatePassVo updatePassVo) {
        return userService.updatePass(request, response, updatePassVo);
    }

    @ApiOperation("修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "String"),
            @ApiImplicitParam(name = "birth", value = "生日", dataType = "String"),
            @ApiImplicitParam(name = "sex", value = "性别", dataType = "String"),
            @ApiImplicitParam(name = "head", value = "头像", dataType = "String"),
    })
    @PostMapping("/updateUserInfo")
    @ResponseBody
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, @Validated InfoVo infoVo) {
        return userService.updateUserInfo(request, response, infoVo);
    }
}
