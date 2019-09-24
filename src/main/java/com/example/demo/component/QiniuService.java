package com.example.demo.component;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class QiniuService {

    @Value("${qiniu.access.key}")
    private String accessKey;

    @Value("${qiniu.secret.key}")
    private String secretKey;

    @Value("${qiniu.bucket.name}")
    private String bucketName;

    @Value("${qiniu.domain.name}")
    private String domainName;

    private Auth auth;
    private Configuration configuration;
    private UploadManager uploadManager;
    private BucketManager bucketManager;

    private Auth getAuth() {
        if (auth == null) {
            auth = Auth.create(accessKey, secretKey);
        }
        return auth;
    }

    private Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration(Region.huanan());
        }
        return configuration;
    }

    private UploadManager getUploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager(getConfiguration());
        }
        return uploadManager;
    }

    private BucketManager getBucketManager() {
        if (bucketManager == null) {
            bucketManager = new BucketManager(getAuth(), getConfiguration());
        }
        return bucketManager;
    }

    // 获取普通上传凭证
    private String getUpToken() {
        return getAuth().uploadToken(bucketName);
    }

    // 获取覆盖上传凭证
    private String getUpToken(String keyName) {
        return getAuth().uploadToken(bucketName, keyName);
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
     * @param data
     * @param fileKey
     * @return
     */
    public String upload(byte[] data, String fileKey) throws QiniuException {
        Response res = getUploadManager().put(data, fileKey, getUpToken(fileKey));
        System.out.println(res);
        DefaultPutRet ret = JSONObject.parseObject(res.bodyString(), DefaultPutRet.class);
        return ret.key;
    }

    /**
     * 删除文件
     * @param fileKey
     * @return
     * @throws QiniuException
     */
    public boolean delete(String fileKey) throws QiniuException {
        Response response = getBucketManager().delete(bucketName, fileKey);
        return response.statusCode == 200 ? true : false;
    }

    /**
     * 获取文件
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
