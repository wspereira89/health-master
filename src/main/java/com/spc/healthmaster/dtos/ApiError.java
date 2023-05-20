package com.spc.healthmaster.dtos;
import com.google.common.collect.ImmutableList;
import com.spc.healthmaster.exception.ApiException;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class ApiError {

    private String error;
    private String message;
    private int status;
    private final List<Cause> causes;
    private boolean expected;

    /* default */ ApiError() {
        this.causes = Collections.emptyList();
        this.expected = false;
    }
    public ApiError(String error, String message, int status) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public ApiError(String error, String message, int status, List<Cause> causes) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.causes = causes == null ? Collections.emptyList() : causes;
    }

    public ApiError withCause(final String causeCode, final String causeDescription) {
        final Cause cause = new Cause(causeCode, causeDescription);
        return new ApiError(error, message, status,  ImmutableList.<Cause>builder().addAll(getCauses()).add(cause).build());
    }

    public ApiError withCause(final List<Cause> newCauses) {
        return new ApiError(error, message, status, ImmutableList.<Cause>builder().addAll(getCauses()).addAll(newCauses).build());
    }

    public ApiError withCause(final ApiError otherApiError) {
        return withCause(otherApiError.getCauses());
    }

    public ApiError asExpected() {
        final ApiError apiError = new ApiError(error, message, status, causes);
        apiError.expected = true;
        return apiError;
    }

    public ApiException toException() {
        return new ApiException(error, message, status, expected, causes);
    }

    public static ApiError fromException(final ApiException e) {
        final ApiError apiError = new ApiError(e.getError(), e.getMessage(), e.getStatus(), e.getCauses());
        apiError.expected = e.isExpected();
        return apiError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return status == apiError.status &&
                Objects.equals(error, apiError.error) &&
                Objects.equals(message, apiError.message) &&
                Objects.equals(expected, apiError.expected) &&
                unorderedListEquals(causes, apiError.causes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, message, status, causes, expected);
    }

    private boolean unorderedListEquals(List<Cause> list1, List<Cause> list2) {
        return list1.containsAll(list2) && list2.containsAll(list1);
    }
}
