package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.QiniuService;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "上传文件控制类")
@Controller
public class UploadController {

    @Autowired
    QiniuService qiniuService;

    @Autowired
    UserService userService;

    @ApiOperation("上传用户头像")
    @RequestMapping("/uploadIcon")
    @ResponseBody
    public Result uploadIcon(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failed(Status.FILE_UPLOAD_ERROR);
        }

        try {
            byte[] bytes = file.getBytes();
            String key = qiniuService.upload(bytes);

            // 成功
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setIcon(key);
            userService.updateUserInfo(request, response, userInfoVo);

            return Result.success("");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(Status.FILE_UPLOAD_ERROR);
        }
    }
}
