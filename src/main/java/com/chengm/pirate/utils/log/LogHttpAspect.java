package com.chengm.pirate.utils.log;

import com.alibaba.fastjson.JSONObject;
import com.chengm.pirate.config.AppConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * program: CmPirate
 * description: 请求拦截
 * author: ChengMo
 * create: 2019-11-30 22:54
 **/
@Aspect
@Component
public class LogHttpAspect {

    private Logger logger = LoggerFactory.getLogger(LogHttpAspect.class);

    private long start;

    // execution  后面填写拦截的controller的路径
    @Pointcut("execution(public * com.chengm.pirate.controller..*.*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        if (!AppConfig.IS_DEBUG) {
            // 非debug模式不打印日志
            return;
        }
        start = System.currentTimeMillis();
        logger.info("api request start " + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.debug("url={}", request.getRequestURI());
        logger.debug("method={}", request.getMethod());
        logger.debug("ip={}", request.getRemoteAddr());

        if (joinPoint != null) {
            logger.trace("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            logger.trace("args={}", JSONObject.toJSON(joinPoint.getArgs()));
        }

        // 打印请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            LogUtil.logValue(name + " : " + value, LogLevel.LEVEL_INFO);
        }
    }

    // 获取接口返回的内容
    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        if (!AppConfig.IS_DEBUG) {
            // 非debug模式不打印日志
            return;
        }
        long end = System.currentTimeMillis();
        logger.warn("time={}", (end - start) + "ms");
        if (object == null) {
            logger.debug("returning={}", "");
        } else {
            logger.debug("returning={}", JSONObject.toJSON(object).toString());
        }
    }

}
