package com.spc.healthmaster.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class LoggingResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(LoggingResponseBodyAdvice.class);

    @Override
    public boolean supports(final MethodParameter returnType, final Class converterType) {
        // Aplica a todos los métodos que retornan ResponseEntity o cualquier tipo de objeto
        return ResponseEntity.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(
            final Object body, MethodParameter returnType,
            final MediaType selectedContentType,
            final Class selectedConverterType, ServerHttpRequest request,
            final ServerHttpResponse response) {
        logger.info("Response: {}", body);
        return body;
    }
}

