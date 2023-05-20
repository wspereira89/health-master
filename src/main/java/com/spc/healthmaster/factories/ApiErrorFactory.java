package com.spc.healthmaster.factories;

import com.spc.healthmaster.dtos.ApiError;

import static javax.servlet.http.HttpServletResponse.SC_BAD_GATEWAY;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public enum ApiErrorFactory {
    ;


    public static final ApiError ALREADY_INITIALIZED =
            new ApiError("already_initialized", "The application was already initialized", SC_BAD_REQUEST).asExpected();

    public static final ApiError ALREADY_STOPPED =
            new ApiError("already_stopped", "The application was already stopped", SC_BAD_REQUEST).asExpected();
    public static final ApiError METHOD_ARGUMENT_NOT_VALID =
            new ApiError("validation_error", "Arguments not valid", SC_BAD_REQUEST).asExpected();
    public static final ApiError DESERIALIZATION_ERROR =
            new ApiError("deserialization_error", "JSON deserialization error", SC_BAD_REQUEST).asExpected();

    public static final ApiError COMMAND_NOT_FOUND =
            new ApiError("command_not_found", "no valid command found", SC_BAD_REQUEST).asExpected();
    public static final ApiError STRATEGY_NOT_FOUND =
            new ApiError("strategy_not_found", "no valid strategy found", SC_BAD_REQUEST).asExpected();

    private static final String NOT_FOUND_ERROR_PREFIX = "not_found_";


    public static ApiError notFoundConnectionSsh(final String serverId) {
        return  new ApiError(NOT_FOUND_ERROR_PREFIX+"server", "not found connection ssh of serverId: "+ serverId, SC_BAD_REQUEST);
    }

    public static ApiError notFoundApplication(final String application) {
        return  new ApiError(NOT_FOUND_ERROR_PREFIX+"application", "not found application: "+ application, SC_BAD_REQUEST);
    }

    public static ApiError sshException(final String name, final String host) {
        return  new ApiError("ssh_connection", "username :["+name+"] could not connect to host:["+host+"]",SC_BAD_GATEWAY);
    }
}
