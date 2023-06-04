package com.spc.healthmaster.factories;

import com.spc.healthmaster.dtos.error.ApiErrorDto;

import static com.spc.healthmaster.constants.Constants.*;
import static javax.servlet.http.HttpServletResponse.*;

public enum ApiErrorFactory {
    ;
    public static final ApiErrorDto METHOD_ARGUMENT_NOT_VALID =
            new ApiErrorDto("validation_error", "Arguments not valid", SC_BAD_REQUEST).asExpected();
    public static final ApiErrorDto DESERIALIZATION_ERROR =
            new ApiErrorDto("deserialization_error", "JSON deserialization error", SC_BAD_REQUEST).asExpected();

    public static final ApiErrorDto COMMAND_NOT_FOUND =
            new ApiErrorDto("command_not_found", "no valid command found", SC_BAD_REQUEST).asExpected();
    public static final ApiErrorDto STRATEGY_NOT_FOUND =
            new ApiErrorDto("strategy_not_found", "no valid strategy found", SC_BAD_REQUEST).asExpected();

    private static final String NOT_FOUND_ERROR_PREFIX = "not_found_";

    public static ApiErrorDto jpaException(final String error) {
        return new ApiErrorDto(JPA_ERROR, error, SC_INTERNAL_SERVER_ERROR);
    }

    public static ApiErrorDto alreadyInitializedException(final String application) {
        return new ApiErrorDto(ALREADY_INITIALIZED_STATUS, "The application [" + application + "] was already initialized", SC_BAD_REQUEST);
    }

    public static ApiErrorDto alreadyStoppedException(final String application) {
        return new ApiErrorDto(ALREADY_STOPPED_STATUS, "The application [" + application + "] was already stopped", SC_BAD_REQUEST);
    }

    public static ApiErrorDto notDeployException(final String application, final String server) {
        return new ApiErrorDto(
                APPLICATION_NOT_DEPLOYED,
                "application [" + application + "] cannot be found deployed on server [" + server + "]",
                SC_NOT_FOUND
        );
    }

    public static ApiErrorDto notFoundServerManager(final Long serverId) {
        return  new ApiErrorDto(NOT_FOUND_ERROR_PREFIX+"server", "not found server Manager: "+ serverId, SC_NOT_FOUND);
    }
    public static ApiErrorDto alreadyExistServerManager(final String server){
        return new ApiErrorDto(ALREADY_EXIST_SSH_MANAGER,"the serverManager [" + server + "] is already registered in the database", SC_BAD_REQUEST);
    }

    public static ApiErrorDto notFoundConnectionSsh(final Long serverId) {
        return  new ApiErrorDto(NOT_FOUND_ERROR_PREFIX+"sshManager", "not found connection ssh of serverId: "+ serverId, SC_NOT_FOUND);
    }

    public static ApiErrorDto notFoundApplication(final Long application) {
        return  new ApiErrorDto(NOT_FOUND_ERROR_PREFIX+"application", "not found application: "+ application, SC_NOT_FOUND);
    }

    public static ApiErrorDto sshException(final String name, final String host) {
        return  new ApiErrorDto(SSH_CONNECTION_STATUS, "username :["+name+"] could not connect to host:["+host+"]",SC_BAD_GATEWAY);
    }

    public static ApiErrorDto alreadyExistSshManager(final String server){
        return new ApiErrorDto(ALREADY_EXIST_SSH_MANAGER,"the sshManager [" + server + "] is already registered in the database", SC_BAD_REQUEST);
    }
}
