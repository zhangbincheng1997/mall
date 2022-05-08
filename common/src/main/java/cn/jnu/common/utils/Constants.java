package cn.jnu.common.utils;

import com.qiniu.util.StringMap;

public class Constants {

    public static final Integer CAPTCHA_WIDTH = 120; // 验证码宽度
    public static final Integer CAPTCHA_HEIGHT = 38; // 验证码高度
    public static final Integer CAPTCHA_LENGTH = 4; //验证码字符长度
    public static final Integer CAPTCHA_EXPIRE = 60; // 验证码过期时间 60s

    public static final String CAPTCHA_KEY = "captcha:";
    public static final String USER_KEY = "user:"; // 用户缓存
    public static final String ROLE_KEY = "role:"; // 角色缓存
    public static final String PERMISSION_KEY = "permission:"; // 权限缓存

    public static final Integer REDIS_CACHE_EXPIRE = 60 * 60; // Redis Cache 缓存过期时间 60*60s
    public static final Integer USER_EXPIRE = 60 * 60; // 用户缓存过期时间 60*60s
    public static final Integer ROLE_EXPIRE = 60 * 60; // 角色缓存过期时间 60*60s
    public static final Integer PERMISSION_EXPIRE = 60 * 60; // 权限缓存过期时间 60*60s

    public static final Integer UPLOAD_RETRY = 3; // 重传次数
    public static final Integer EXPIRE_SECONDS = 60 * 60; // 过期时间
    public static final StringMap PUT_POLICY = new StringMap().put("returnBody", // 返回格式
            "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":\"$(fsize)\",\"width\":\"$(imageInfo.width)\", \"height\":\"$(imageInfo.height)\"}");
}
