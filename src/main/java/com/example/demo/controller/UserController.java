package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.access.AccessLimit;
import com.example.demo.component.QiniuService;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserDto;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Api(tags = "用户控制类")
@Slf4j
@Validated
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@Validated UserDto userDto) {
        // 验证用户名密码
        User user = userService.register(userDto);
        return Result.success(user);
    }

    @ApiOperation("获取信息")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserInfo(Principal principal) {
        String username = principal.getName(); // SecurityContextHolder上下文
        User user = userService.getUserByUsername(username);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return Result.success(userInfoVo);
    }

    @ApiOperation("修改信息")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserInfo(Principal principal, @Validated UserInfoDto userInfoDto) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userService.updateUserInfoByUsername(username, userInfoDto);
        return Result.success();
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/user/password", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(Principal principal, @RequestParam("password") String password) {
        String username = principal.getName(); // SecurityContextHolder上下文
        userService.updatePasswordByUsername(username, password);
        return Result.success();
    }

    @ApiOperation("修改头像 3次/分钟")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")})
    @RequestMapping(value = "/user/icon", method = RequestMethod.POST)
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 3)
    public Result uploadIcon(Principal principal, @RequestParam("file") MultipartFile file) {
        String username = principal.getName(); // SecurityContextHolder上下文
        if (!file.isEmpty()) {
            try {
                String path = qiniuService.upload(file.getBytes());
                userService.updateAvatarByUsername(username, path);
                return Result.success(path);
            } catch (IOException e) {
                log.error("上传用户头像失败", e);
            }
        }
        return Result.failure();
    }
}
