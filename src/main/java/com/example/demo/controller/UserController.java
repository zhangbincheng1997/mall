package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.service.UserService;
import com.example.demo.vo.LoginVo;
import com.example.demo.vo.UpdatePassVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/register")
    @ResponseBody
    public Result register(@Valid LoginVo loginVo) {
        return userService.register(loginVo);
    }

    @RequestMapping("/login")
    @ResponseBody
    public Result login(HttpServletResponse response, @Valid LoginVo loginVo) {
        return userService.login(response, loginVo);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public Result getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        return userService.getUserByToken(request, response);
    }

    @RequestMapping("/updatePass")
    @ResponseBody
    public Result updatePass(HttpServletRequest request, @Valid UpdatePassVo updatePassVo) {
        return userService.updatePass(request, updatePassVo);
    }
}
