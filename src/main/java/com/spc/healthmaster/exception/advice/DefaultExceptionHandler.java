package com.spc.healthmaster.exception.advice;

import com.spc.healthmaster.dtos.ApiErrorDto;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(final ApiException apiException) {
        return new ResponseEntity<>(ApiErrorDto.fromException(apiException), HttpStatus.valueOf(apiException.getStatus()));
    }

}
