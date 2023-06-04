package com.spc.healthmaster.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SwaggerServerdResponseConstants {

    public static final String SERVER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID = "{\n" +
            "  \"error\": \"validation_error\",\n" +
            "  \"message\": \"Arguments not valid\",\n" +
            "  \"status\": 400,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "      \"code\": \"user\",\n" +
            "      \"description\": \"Invalid  user\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"host\",\n" +
            "      \"description\": \"Invalid Host\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"password\",\n" +
            "      \"description\": \"Invalid passwrod\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"serverName\",\n" +
            "      \"description\": \"Invalid  server Name\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String EDIT_SERVER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID_ID = "{\n" +
            "  \"error\": \"validation_error\",\n" +
            "  \"message\": \"Arguments not valid\",\n" +
            "  \"status\": 400,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "    \"code\": \"id\",\n" +
            "    \"description\": \"Invalid Id\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String SERVER_ERROR_502_CONNECTION_SSH = "{\n" +
            "  \"error\": \"ssh_connection\",\n" +
            "  \"message\": \"username :[] could not connect to host:[]\",\n" +
            "  \"status\": 502,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "      \"code\": \"session\",\n" +
            "      \"description\": \"java.net.ConnectException: Connection refused: connect\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    public static final String EDIT_SERVER_ERROR_400_DESERIALIZATION_UNKNOWN = "{\n" +
            "  \"error\": \"deserialization_error\",\n" +
            "  \"message\": \"JSON deserialization error\",\n" +
            "  \"status\": 400,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "      \"code\": \"Long\",\n" +
            "      \"description\": \"must be one of: [Cannot deserialize value of type `java.lang.Long` from String \\\"x\\\": not a valid `java.lang.Long` value\\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 2, column: 9] (through reference chain: com.spc.healthmaster.dtos.RequestServerDto[\\\"id\\\"])]\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static final String SERVER_ERROR_400_ALREADY_EXIST_SSHMANAGER = "{\n" +
            "    \"error\": \"already_exist_ssManager\",\n" +
            "    \"message\": \"the sshManager [SERVERNAME] is already registered in the database\",\n" +
            "    \"status\": 400,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String EDIT_SERVER_ERROR_404_NOT_FOUND_SSHMANAGER = "{\n" +
            "    \"error\": \"not_found_sshManager\",\n" +
            "    \"message\": \"not found server Manager: 1\",\n" +
            "    \"status\": 404,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String SERVER_ERROR_500_BD =  "{\n" +
            "    \"error\": \"jpa_error\",\n" +
            "    \"message\": \"Error en base datos\",\n" +
            "    \"status\": 500,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String SERVER_ERROR_500 = "{\"error\":\"internal_server_error\",\"message\":\"Internal server error\",\"status\":500,\"expected\":true,\"causes\":[]}";
}
