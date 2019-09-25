package com.example.demo.component;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class QiniuService implements InitializingBean {

    @Value("${qiniu.bucket.name}")
    private String bucketName;

    @Value("${qiniu.domain.name}")
    private String domainName;

    @Autowired
    private Auth auth;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    private StringMap putPolicy;

    private long expireSeconds ;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":\"$(fsize)\",\"width\":\"$(imageInfo.width)\", \"height\":\"$(imageInfo.height)\"}");
        this.expireSeconds = 3600;
    }

    // 获取上传凭证
    private String getUploadToken(String keyName) {// expires policy
        return auth.uploadToken(bucketName, keyName,  expireSeconds, putPolicy);
    }

    /**
     * 上传二进制 普通上传
     *
     * @param data
     * @return
     */
    public String upload(byte[] data) throws QiniuException {
        return upload(data, null);
    }

    /**
     * 上传二进制 覆盖上传
     *
     * @param file
     * @param fileKey
     * @return
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
        String path = domainName + ret.key;
        return path; // return response.bodyString();
    }

    /**
     * 删除文件
     *
     * @param fileKey
     * @return
     * @throws QiniuException
     */
    public Response  delete(String fileKey) throws QiniuException {
        Response response = bucketManager.delete(bucketName, fileKey);
        // 重传机制 默认3次
        int retry = 0;
        while (response.needRetry() && retry++ < Constants.UPLOAD_RETRY) {
            response = bucketManager.delete(bucketName, fileKey);
        }
        return response;
    }

    /**
     * 获取文件
     *
     * @param fileKey
     * @return
     * @throws Exception
     */
    public String get(String fileKey) throws Exception {
        String encodedFileName = URLEncoder.encode(fileKey, "utf-8").replace("+", "%20");
        String url = String.format("%s/%s", domainName, encodedFileName);
        return url;
    }
}
