package com.example.demo.controller;

import com.example.demo.access.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisService;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Api(tags = "邮箱验证码控制类")
@Controller
@Validated
public class EmailController {

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    /**
     * 发送邮箱验证码
     * 限制每分钟请求一次
     *
     * @param to
     * @return
     */
    @ApiOperation("发送邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "to", value = "接收者邮箱", required = true, dataType = "String")
    })
    @RequestMapping("sendEmail")
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 1, needLogin = false)
    public Result sendEmail(@RequestParam @Email String to) {
        rabbitSender.send(Constants.EMAIL_TOPIC, to);
        return Result.success("");
    }

    /**
     * 验证邮箱验证码
     * 限制每分钟请求三次
     *
     * @param to
     * @param code
     * @return
     */
    @ApiOperation("验证邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "to", value = "接收者邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping("checkEmail")
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 3, needLogin = false)
    public Result checkEmail(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam @Email String to,
                             @RequestParam @Size(min = 4, max = 4) String code) {
        String redisCode = (String) redisService.get(Constants.EMAIL_KEY + "_" + to);
        if (redisCode == null) {
            return Result.failed(Status.CODE_EXPIRED);
        } else if (!redisCode.equals(code)) {
            return Result.failed(Status.CODE_ERROR);
        } else {
            // 成功
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setEmail(to);
            userService.updateUserInfo(request, response, userInfoVo);
            return Result.success("");
        }
    }
}
