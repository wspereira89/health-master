package com.spc.healthmaster.exception.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.spc.healthmaster.dtos.CauseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.DESERIALIZATION_ERROR;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        final List<CauseDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( x -> new CauseDto(x.getField() , x.getDefaultMessage())
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(METHOD_ARGUMENT_NOT_VALID.withCause(errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        final Throwable mostSpecificCause = ex.getMostSpecificCause();
        String errorMessage = "must be one of";
        String fieldError = "unknown";
        if(mostSpecificCause instanceof InvalidFormatException) {
           final InvalidFormatException invalidFormatException = (InvalidFormatException) mostSpecificCause;
           fieldError = invalidFormatException.getTargetType().getSimpleName();
           List<String> acceptedValues = Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.toList());
           errorMessage +=  ": " + acceptedValues;
        }

        return new ResponseEntity<>(DESERIALIZATION_ERROR.withCause(fieldError, errorMessage), HttpStatus.BAD_REQUEST);
    }


}