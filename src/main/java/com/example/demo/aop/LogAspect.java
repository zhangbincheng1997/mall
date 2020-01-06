package com.example.demo.aop;

import com.example.demo.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * com.example.demo.controller.*.*(..))")
    public void logPointCut() {
    }

    /**
     * 在切点前执行
     *
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String ip = IpUtil.getClientIP(request);
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String parameters = Arrays.toString(joinPoint.getArgs());
        log.info("REQUEST URL:" + url + " | HTTP METHOD: " + httpMethod + " | IP: " + ip + " | CLASS_METHOD: " + classMethod
                + " | ARGS:" + parameters);
    }

    /**
     * 在切点后，return前执行
     *
     * @param joinPoint
     */
    @After("logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
    }

    /**
     * 在切入点，return后执行，如果相对某些方法的返回参数进行处理，可以在此处执行
     *
     * @param object
     */
    @AfterReturning(returning = "object", pointcut = "logPointCut()")
    public void doAfterReturning(Object object) {
        log.info("RESPONSE TIME: " + (System.currentTimeMillis() - startTime.get()) + "ms");
        log.info("RESPONSE BODY: " + object);
    }
}
