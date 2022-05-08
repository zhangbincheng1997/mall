package cn.jnu.common.component.qiniu;

import cn.jnu.common.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class QiniuService {

    private final Auth auth;
    private final BucketManager bucketManager;
    private final QiniuConfig qiniuConfig;
    private final UploadManager uploadManager;

    // 获取上传凭证
    private String getUploadToken(String keyName) {
        return auth.uploadToken(qiniuConfig.getBucketName(), keyName, Constants.EXPIRE_SECONDS, Constants.PUT_POLICY);
    }

    /**
     * 上传文件 普通上传
     */
    public String upload(byte[] file) throws QiniuException {
        return upload(file, null);
    }

    /**
     * 上传文件 覆盖上传
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
        return qiniuConfig.getDomainName() + ret.key;
    }

    /**
     * 删除文件
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
