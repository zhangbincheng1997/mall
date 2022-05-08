package cn.jnu.server.compoment;

import cn.jnu.common.base.GlobalException;
import cn.jnu.common.base.Result;
import cn.jnu.common.base.ResultCode;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;

@Slf4j
@Service
@AllArgsConstructor
public class RequestService {

    private final RestTemplate restTemplate;

    private <T> T request(HttpMethod method, String api, String body, ParameterizedTypeReference<T> reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<T> response = restTemplate.exchange(api, method, httpEntity, reference);
        System.out.println(response);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new GlobalException(ResultCode.FAILURE.getCode(), response.toString());
        }
        return response.getBody();
    }

    // 泛型...
    private <T> ParameterizedTypeReference<Result<T>> getReference(Class<T> clazz) {
        ParameterizedTypeImpl type = ParameterizedTypeImpl.make(Result.class, new Type[]{clazz}, Result.class.getDeclaringClass());
        return ParameterizedTypeReference.forType(type);
    }

    /**
     * GET请求
     *
     * @param api 请求地址
     * @return 请求结果
     */
    public JSONObject get(String api) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.GET, api, null, new ParameterizedTypeReference<JSONObject>() {
        });
    }

    /**
     * GET请求（泛型）
     *
     * @param api 请求地址
     * @return 请求结果
     */
    public <T> T get(String api, Class<T> clazz) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.GET, api, null, getReference(clazz)).getData();
    }

    /**
     * GET请求（自定义泛型）
     *
     * @param api 请求地址
     * @return 请求结果
     */
    public <T> T get(String api, ParameterizedTypeReference<T> reference) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.GET, api, null, reference);
    }

    /**
     * POST请求
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public JSONObject post(String api, JSONObject body) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.POST, api, body.toString(), new ParameterizedTypeReference<JSONObject>() {
        });
    }

    /**
     * POST请求（泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T post(String api, JSONObject body, Class<T> clazz) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.POST, api, body.toString(), getReference(clazz)).getData();
    }

    /**
     * POST请求（自定义泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T post(String api, JSONObject body, ParameterizedTypeReference<T> reference) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.POST, api, body.toString(), reference);
    }

    /**
     * PUT请求
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public JSONObject put(String api, JSONObject body) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.PUT, api, body.toString(), new ParameterizedTypeReference<JSONObject>() {
        });
    }

    /**
     * PUT请求（泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T put(String api, JSONObject body, Class<T> clazz) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.PUT, api, body.toString(), getReference(clazz)).getData();
    }

    /**
     * PUT请求（自定义泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T put(String api, JSONObject body, ParameterizedTypeReference<T> reference) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.PUT, api, body.toString(), reference);
    }

    /**
     * DELETE请求
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public JSONObject delete(String api, JSONObject body) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.DELETE, api, body.toString(), new ParameterizedTypeReference<JSONObject>() {
        });
    }

    /**
     * DELETE请求（泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T delete(String api, JSONObject body, Class<T> clazz) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.DELETE, api, body.toString(), getReference(clazz)).getData();
    }

    /**
     * DELETE请求（自定义泛型）
     *
     * @param api  请求地址
     * @param body JSON body
     * @return 请求结果
     */
    public <T> T delete(String api, JSONObject body, ParameterizedTypeReference<T> reference) {
        if (StringUtils.isEmpty(api)) throw new GlobalException(ResultCode.FAILURE);
        return request(HttpMethod.DELETE, api, body.toString(), reference);
    }
}
