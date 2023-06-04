package com.spc.healthmaster.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.spc.healthmaster.controller.*.*(..))")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void beforeMethodExecution(final JoinPoint joinPoint) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String url = request.getRequestURL().toString();
        final String httpMethod = request.getMethod();
        final String methodName = joinPoint.getSignature().getName();
        final String requestBody = getRequestBody(joinPoint.getArgs());
        final HttpHeaders headers = getHeaders();
        logger.info("Request received. \nURL: {}\nMethod:{}\nHTTP Method: {}\nHeader: {}\nRequest Body: {}\n ", url, methodName, headers, httpMethod, requestBody);
    }

    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void afterMethodExecution(final Object result) {
        logger.info("Response: {}", result);
    }

    private String getRequestBody(final Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> !(arg instanceof MultipartFile)) // Excluir archivos adjuntos
                .map(arg -> {
                    try {
                        return objectMapper.writeValueAsString(arg);
                    } catch (JsonProcessingException e) {
                        logger.error("Error while serializing request body", e);
                        return null;
                    }
                })
                .filter(body -> body != null)
                .collect(Collectors.joining(", "));
    }

    private HttpHeaders getHeaders() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final Enumeration<String> headerNames = request.getHeaderNames();
        final HttpHeaders headers = new HttpHeaders();

        Collections.list(headerNames).forEach(headerName -> {
            String headerValue = request.getHeader(headerName);
            headers.add(headerName, headerValue);
        });

        return headers;
    }
}

