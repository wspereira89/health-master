package com.spc.healthmaster.exception;

import com.spc.healthmaster.dtos.Cause;
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
    private final boolean expected;
    private final List<Cause> causes;

    /**
     * ApiException constructor
     *
     * @param error the error code
     * @param message the error message
     * @param status the http status
     * @param expected boolean telling if this is expected
     */
    public ApiException(final String error, final String message, final int status, final boolean expected) {
        this(error, message, status, expected, (List<Cause>) null);
    }

    /**
     * ApiException constructor that sets exception cause
     *
     * @param error the error code
     * @param message the error message
     * @param status the http status
     * @param expected boolean telling if this is expected
     * @param throwable the cause
     */
    public ApiException(final String error, final String message, final int status, final boolean expected, final Throwable throwable) {
        this(error, message, status, expected);
        initCause(throwable);
    }

    /**
     * Creates a new ApiException
     * @param error the code
     * @param message the description
     * @param status the statusCode
     * @param expected the expected
     * @param causes the causes
     */
    public ApiException(final String error, final String message, final int status, final boolean expected, final List<Cause> causes) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
        this.expected = expected;
        this.causes = causes;
    }

}
