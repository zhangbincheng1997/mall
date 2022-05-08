package cn.jnu.common.component.qiniu;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "qiniu")
@Configuration
public class QiniuConfig {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Value("${qiniu.domainName}")
    private String domainName;

    private com.qiniu.storage.Configuration getConfiguration() {
        return new com.qiniu.storage.Configuration(Region.huanan());
    }

    @Bean
    public UploadManager getUploadManager() {
        return new UploadManager(getConfiguration());
    }

    @Bean
    public Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public BucketManager getBucketManager() {
        return new BucketManager(getAuth(), getConfiguration());
    }
}
