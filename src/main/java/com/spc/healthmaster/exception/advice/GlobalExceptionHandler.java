package com.spc.healthmaster.exception.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.spc.healthmaster.dtos.error.CauseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.DESERIALIZATION_ERROR;
import static com.spc.healthmaster.factories.ApiErrorFactory.METHOD_ARGUMENT_NOT_VALID;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {

        return new ResponseEntity<>(METHOD_ARGUMENT_NOT_VALID.withCause("convert", ex.getMessage()) , HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final List<CauseDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( x -> new CauseDto(x.getField() , x.getDefaultMessage())
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(METHOD_ARGUMENT_NOT_VALID.withCause(errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final Throwable mostSpecificCause = ex.getMostSpecificCause();
        String errorMessage = "must be one of";
        String fieldError = "unknown";
        if(mostSpecificCause instanceof InvalidFormatException) {
           final InvalidFormatException invalidFormatException = (InvalidFormatException) mostSpecificCause;
           fieldError = invalidFormatException.getTargetType().getSimpleName();
            List<String> acceptedValues = null;
           if(invalidFormatException.getTargetType().getEnumConstants() != null) {
               acceptedValues =  Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                       .map(Object::toString)
                       .collect(Collectors.toList());
           } else {
               acceptedValues = Arrays.asList(mostSpecificCause.getMessage());
           }

           errorMessage +=  ": " + acceptedValues;
        }

        return new ResponseEntity<>(DESERIALIZATION_ERROR.withCause(fieldError, errorMessage), HttpStatus.BAD_REQUEST);
    }


}