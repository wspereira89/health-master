package com.spc.healthmaster.exception.advice;

import com.spc.healthmaster.dtos.ApiError;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(final ApiException apiException) {
        return new ResponseEntity<>(ApiError.fromException(apiException), HttpStatus.valueOf(apiException.getStatus()));
    }

}
