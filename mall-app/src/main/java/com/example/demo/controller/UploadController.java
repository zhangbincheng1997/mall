package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "上传文件")
@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("上传文件 限制ip:3次/分钟")
    @PostMapping("")
    @ResponseBody
    public Result<String> uploadIcon(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = qiniuService.upload(file.getBytes());
                return Result.success(path);
            } catch (IOException e) {
                return Result.failure(Status.FILE_UPLOAD_ERROR);
            }
        }
        return Result.failure(Status.FILE_UPLOAD_EMPTY);
    }
}
