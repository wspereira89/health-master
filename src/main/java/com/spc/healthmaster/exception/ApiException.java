package com.spc.healthmaster.exception;

import com.spc.healthmaster.dtos.error.CauseDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ApiException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String error;
    private final String message;
    private final int status;
    private final List<CauseDto> causeDtos;

    /**
     * ApiException constructor
     *
     * @param error the error code
     * @param message the error message
     * @param status the http status
     */
    public ApiException(final String error, final String message, final int status) {
        this(error, message, status, (List<CauseDto>) null);
    }

    /**
     * ApiException constructor that sets exception cause
     *
     * @param error the error code
     * @param message the error message
     * @param status the http status
     * @param throwable the cause
     */
    public ApiException(final String error, final String message, final int status, final Throwable throwable) {
        this(error, message, status);
        initCause(throwable);
    }

    /**
     * Creates a new ApiException
     * @param error the code
     * @param message the description
     * @param status the statusCode
     * @param causeDtos the causes
     */
    public ApiException(final String error, final String message, final int status, final List<CauseDto> causeDtos) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
        this.causeDtos = causeDtos;
    }

}
