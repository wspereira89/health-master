package com.spc.healthmaster.dtos.error;

import com.google.common.collect.ImmutableList;
import com.spc.healthmaster.exception.ApiException;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class ApiErrorDto {
    private String error;
    private String message;
    private int status;

    private final List<CauseDto> causes;

    /* default */ ApiErrorDto() {
        this.causes = Collections.emptyList();
    }
    public ApiErrorDto(String error, String message, int status) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public ApiErrorDto(String error, String message, int status, List<CauseDto> causes) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.causes = causes == null ? Collections.emptyList() : causes;
    }

    public ApiErrorDto withCause(final String causeCode, final String causeDescription) {
        final CauseDto causeDto = new CauseDto(causeCode, causeDescription);
        return new ApiErrorDto(error, message, status,  ImmutableList.<CauseDto>builder().addAll(getCauses()).add(causeDto).build());
    }

    public ApiErrorDto withCause(final List<CauseDto> newCauseDtos) {
        return new ApiErrorDto(error, message, status, ImmutableList.<CauseDto>builder().addAll(getCauses()).addAll(newCauseDtos).build());
    }

    public ApiErrorDto withCause(final ApiErrorDto otherApiErrorDto) {
        return withCause(otherApiErrorDto.getCauses());
    }

    public ApiErrorDto asExpected() {
        final ApiErrorDto apiErrorDto = new ApiErrorDto(error, message, status, causes);

        return apiErrorDto;
    }

    public ApiException toException() {
        return new ApiException(error, message, status,  causes);
    }

    public static ApiErrorDto fromException(final ApiException e) {
        return new ApiErrorDto(e.getError(), e.getMessage(), e.getStatus(), e.getCauseDtos());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorDto apiErrorDto = (ApiErrorDto) o;
        return status == apiErrorDto.status &&
                Objects.equals(error, apiErrorDto.error) &&
                Objects.equals(message, apiErrorDto.message) &&
                unorderedListEquals(causes, apiErrorDto.causes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, message, status, causes);
    }

    private boolean unorderedListEquals(List<CauseDto> list1, List<CauseDto> list2) {
        return list1.containsAll(list2) && list2.containsAll(list1);
    }
}
