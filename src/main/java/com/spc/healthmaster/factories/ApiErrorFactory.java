package com.spc.healthmaster.factories;

import com.spc.healthmaster.dtos.ApiErrorDto;

import static com.spc.healthmaster.constants.Constants.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_GATEWAY;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public enum ApiErrorFactory {
    ;


    public static final ApiErrorDto ALREADY_INITIALIZED =
            new ApiErrorDto(ALREADY_INITIALIZED_STATUS, "The application was already initialized", SC_BAD_REQUEST).asExpected();

    public static final ApiErrorDto ALREADY_STOPPED =
            new ApiErrorDto(ALREADY_STOPPED_STATUS, "The application was already stopped", SC_BAD_REQUEST).asExpected();
    public static final ApiErrorDto METHOD_ARGUMENT_NOT_VALID =
            new ApiErrorDto("validation_error", "Arguments not valid", SC_BAD_REQUEST).asExpected();
    public static final ApiErrorDto DESERIALIZATION_ERROR =
            new ApiErrorDto("deserialization_error", "JSON deserialization error", SC_BAD_REQUEST).asExpected();

    public static final ApiErrorDto COMMAND_NOT_FOUND =
            new ApiErrorDto("command_not_found", "no valid command found", SC_BAD_REQUEST).asExpected();
    public static final ApiErrorDto STRATEGY_NOT_FOUND =
            new ApiErrorDto("strategy_not_found", "no valid strategy found", SC_BAD_REQUEST).asExpected();

    private static final String NOT_FOUND_ERROR_PREFIX = "not_found_";


    public static ApiErrorDto notFoundConnectionSsh(final Long serverId) {
        return  new ApiErrorDto(NOT_FOUND_ERROR_PREFIX+"server", "not found connection ssh of serverId: "+ serverId, SC_BAD_REQUEST);
    }

    public static ApiErrorDto notFoundApplication(final Long application) {
        return  new ApiErrorDto(NOT_FOUND_ERROR_PREFIX+"application", "not found application: "+ application, SC_BAD_REQUEST);
    }

    public static ApiErrorDto sshException(final String name, final String host) {
        return  new ApiErrorDto(SSH_CONNECTION_STATUS, "username :["+name+"] could not connect to host:["+host+"]",SC_BAD_GATEWAY);
    }
}
