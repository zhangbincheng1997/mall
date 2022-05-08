package cn.jnu.common.controller;

import cn.jnu.common.base.Result;
import cn.jnu.common.base.ResultCode;
import cn.jnu.common.component.qiniu.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "上传文件")
@RestController
@RequestMapping("/upload")
@AllArgsConstructor
public class UploadController {

    private QiniuService qiniuService;

    @ApiOperation("上传文件")
    @PostMapping("")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = qiniuService.upload(file.getBytes());
                return Result.success(path);
            } catch (IOException e) {
                return Result.failure(ResultCode.FILE_UPLOAD_ERROR);
            }
        }
        return Result.failure(ResultCode.FILE_UPLOAD_EMPTY);
    }
}
