package com.example.demo.controller;

import com.example.demo.aop.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.component.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "文件上传类")
@Slf4j
@Validated
@Controller
public class UploadController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("上传文件 限制ip:3次/分钟")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @AccessLimit(ip = true, time = 60, count = 3)
    public Result uploadIcon(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = qiniuService.upload(file.getBytes());
                return Result.success(path);
            } catch (IOException e) {
                log.error("上传文件失败", e);
            }
        }
        return Result.failure();
    }
}
