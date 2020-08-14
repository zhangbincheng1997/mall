package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.qiniu.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "上传文件")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("上传文件")
    @PostMapping("")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
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
