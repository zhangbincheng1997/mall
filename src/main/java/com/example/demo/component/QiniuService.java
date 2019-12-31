package com.example.demo.component;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.QiniuConfig;
import com.example.demo.utils.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class QiniuService implements InitializingBean {

    @Autowired
    private Auth auth;

    @Autowired
    private QiniuConfig qiniuConfig;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    private long expireSeconds;

    private StringMap putPolicy;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.expireSeconds = 3600;
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":\"$(fsize)\",\"width\":\"$(imageInfo.width)\", \"height\":\"$(imageInfo.height)\"}");
    }

    // 获取上传凭证
    private String getUploadToken(String keyName) {
        return auth.uploadToken(qiniuConfig.getBucketName(), keyName, expireSeconds, putPolicy);
    }

    /**
     * 上传文件 普通上传
     *
     * @param file
     * @return
     * @throws QiniuException
     */
    public String upload(byte[] file) throws QiniuException {
        return upload(file, null);
    }

    /**
     * 上传文件 覆盖上传
     *
     * @param file
     * @param fileKey
     * @return
     * @throws QiniuException
     */
    public String upload(byte[] file, String fileKey) throws QiniuException {
        Response response = uploadManager.put(file, fileKey, getUploadToken(fileKey));
        // 重传机制 默认3次
        int retry = 0;
        while (response.needRetry() && retry < Constants.UPLOAD_RETRY) {
            response = uploadManager.put(file, fileKey, getUploadToken(fileKey));
            retry++;
        }
        DefaultPutRet ret = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
        String path = qiniuConfig.getDomainName() + ret.key;
        return path; // return response.bodyString();
    }

    /**
     * 删除文件
     *
     * @param fileKey
     * @return
     * @throws QiniuException
     */
    public Response delete(String fileKey) throws QiniuException {
        Response response = bucketManager.delete(qiniuConfig.getBucketName(), fileKey);
        // 重传机制 默认3次
        int retry = 0;
        while (response.needRetry() && retry++ < Constants.UPLOAD_RETRY) {
            response = bucketManager.delete(qiniuConfig.getBucketName(), fileKey);
        }
        return response;
    }
}
