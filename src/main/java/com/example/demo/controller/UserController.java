package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.access.AccessLimit;
import com.example.demo.base.Status;
import com.example.demo.component.QiniuService;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisService;
import com.example.demo.access.JwtTokenService;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import com.example.demo.vo.UserInfoVo;
import com.example.demo.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import java.io.IOException;

@Api(tags = "用户控制类")
@Slf4j
@Validated
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@Validated UserVo userVo) {
        // 邮箱验证
        String redisCode = (String) redisService.get(Constants.EMAIL_KEY + userVo.getEmail());
        if (redisCode == null || !redisCode.equals(userVo.getCode())) {
            return Result.failure(Status.CODE_ERROR);
        }
        // 验证用户名密码
        User user = userService.register(userVo);
        if (user == null) {
            return Result.failure(Status.USERNAME_EXIST);
        }
        return Result.success(user);
    }

    @ApiOperation("修改密码 需要邮箱验证")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(@Validated UserVo userVo) {
        // 邮箱验证
        String redisCode = (String) redisService.get(Constants.EMAIL_KEY + userVo.getEmail());
        if (redisCode == null || !redisCode.equals(userVo.getCode())) {
            return Result.failure(Status.CODE_ERROR);
        }
        // 更新密码
        int count = userService.updatePasswordByEmail(userVo);
        if (count != 1) {
            return Result.failure(Status.USERNAME_NOT_EXIST);
        }
        return Result.success();
    }

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserInfo(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader); // JWT
        String username = jwtTokenService.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.failure(Status.USERNAME_NOT_EXIST);
        }
        return Result.success(user);
    }

    @ApiOperation("修改用户信息")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserInfo(HttpServletRequest request, @Validated UserInfoVo userInfoVo) {
        String token = request.getHeader(tokenHeader); // JWT
        String username = jwtTokenService.getUsernameFromToken(token);
        int count = userService.updateUserInfoByUsername(username, userInfoVo);
        if (count != 1) {
            return Result.failure(Status.USERNAME_NOT_EXIST);
        }
        return Result.success();
    }

    @ApiOperation("上传用户头像")
    @RequestMapping(value = "/user/icon", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadIcon(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String result = qiniuService.upload(file.getBytes());
                return Result.success(result);
            } catch (IOException e) {
                log.error("上传用户头像失败", e);
            }
        }
        return Result.failure();
    }

    @ApiOperation("发送邮箱验证码 1次/分")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")})
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 1)
    public Result sendEmail(@RequestParam @Email String email) {
        rabbitSender.send(Constants.EMAIL_TOPIC, email);
        return Result.success();
    }
}
