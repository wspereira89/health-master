package com.spc.healthmaster.constants.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerResponseConstant {

    public static final String RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID = "{\n" +
            "  \"error\": \"validation_error\",\n" +
            "  \"message\": \"Arguments not valid\",\n" +
            "  \"status\": 400,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "      \"code\": \"typeStrategy\",\n" +
            "      \"description\": \"Invalid typeStrategy value, Must be one of [TOMCAT_SERVER, GLASSFISH_SERVER]\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"serverManagerName\",\n" +
            "      \"description\": \"Invalid serverManagerName\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"username\",\n" +
            "      \"description\": \"Invalid username\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"password\",\n" +
            "      \"description\": \"Invalid password\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"port\",\n" +
            "      \"description\": \"Invalid port\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"ssManagerId\",\n" +
            "      \"description\": \"Invalid ssManagerId\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String ERROR_400_ALREADY_EXIST_SERVER_MANAGER = "{\n" +
            "    \"error\": \"already_exist_server_manager\",\n" +
            "    \"message\": \"the serverManager [SERVERNAME] is already registered in the database\",\n" +
            "    \"status\": 400,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String ERROR_404_NOT_FOUND_SERVER_MANAGER = "{\n" +
            "    \"error\": \"not_found_server\",\n" +
            "    \"message\": \"not found server Manager: \",\n" +
            "    \"status\": 404,\n" +
            "    \"causes\": []\n" +
            "}";
}
