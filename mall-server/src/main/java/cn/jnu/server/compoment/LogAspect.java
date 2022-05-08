package cn.jnu.server.compoment;

import cn.hutool.extra.servlet.ServletUtil;
import cn.jnu.common.model.Log;
import cn.jnu.security.jwt.JwtUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

// Filter-> Interceptor->Aspect->Controller……

@Slf4j
@Aspect
@Component
public class LogAspect {

    // 定义切点，新增排除文件上传切点
    @Pointcut("execution(* cn.jnu.*.controller..*(..)) && !execution(public * cn.jnu.*.controller.WebSocketUploadServer.*(..)) )")
    public void logService() {

    }

    @Around("logService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取request
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(sra).getRequest();

        // 基本信息
        Log info = new Log();
        info.setIp(ServletUtil.getClientIP(request));
        info.setUri(request.getRequestURI());
        info.setMethod(request.getMethod());

        // 操作用户
        info.setUserId(JwtUtils.getUserId());
        info.setUsername(JwtUtils.getUsername());

        // 获取signature
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        // 类名
        String className = signature.getDeclaringTypeName();
        String classCommentName = methodSignature.getMethod().getDeclaringClass().getAnnotation(Api.class).tags()[0];
        info.setClassName(className + "[" + classCommentName + "]");

        // 方法名
        String methodName = signature.getName();
        String methodCommentName = methodSignature.getMethod().getAnnotation(ApiOperation.class).value();
        info.setMethodName(methodName + "[" + methodCommentName + "]");


        // 参数名
        String[] parameterNames = methodSignature.getParameterNames();
        info.setParameterNames(parameterNames);

        // 参数值
        List<Object> argList = new ArrayList<>();
        for (Object arg : pjp.getArgs()) {
            // request/response无法使用toJSON
            if (arg instanceof HttpServletRequest) {
                argList.add("HttpServletRequest");
            } else if (arg instanceof HttpServletResponse) {
                argList.add("HttpServletResponse");
            } else if (arg instanceof MultipartFile) {
                argList.add("MultipartFile");
            } else {
                argList.add(JSON.toJSON(arg));
            }
        }
        info.setArgList(argList);

        // 打印日志1
        log.info("请求参数 | {}", JSON.toJSONString(info));

        // 记录开始时间点
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 业务逻辑
        Object result = pjp.proceed();

        // 记录结束时间点
        stopwatch.stop();

        // 打印日志2
        try {
            log.info("返回结果 | {}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.info("返回结果 | {}", e.getMessage());
        }
        log.info("{}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }
}
