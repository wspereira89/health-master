package com.spc.healthmaster.constants.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GeneralConstant {
    public static final String EDIT_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID_ID = "{\n" +
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
    public static final String ERROR_400_DESERIALIZATION_UNKNOWN = "{\n" +
            "  \"error\":\"deserialization_error\",\n" +
            "  \"message\":\"JSON deserialization error\",\n" +
            "  \"status\":400,\n" +
            "  \"causes\":[{\"code\":\"unknown\",\"description\":\"must be one of\"}]\n" +
            "}";

    public static final String ERROR_400_DESERIALIZATION_UNKNOWN_ID = "{\n" +
            "  \"error\": \"deserialization_error\",\n" +
            "  \"message\": \"JSON deserialization error\",\n" +
            "  \"status\": 400,\n" +
            "  \"causes\": [\n" +
            "    {\n" +
            "      \"code\": \"Long\",\n" +
            "      \"description\": \"must be one of: [Cannot deserialize value of type `java.lang.Long` from String \\\"x\\\": not a valid `java.lang.Long` value\\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 2, column: 9] (through reference chain: com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto[\\\"id\\\"])]\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";



    public static final String ERROR_404_NOT_FOUND_SSHMANAGER = "{\n" +
            "    \"error\": \"not_found_sshManager\",\n" +
            "    \"message\": \"not found server Manager: 1\",\n" +
            "    \"status\": 404,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String ERROR_500_BD =  "{\n" +
            "    \"error\": \"jpa_error\",\n" +
            "    \"message\": \"Error en base datos\",\n" +
            "    \"status\": 500,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String ERROR_500_INTERNAL_SERVER = "{\"error\":\"internal_server_error\",\"message\":\"Internal server error\",\"status\":500,\"expected\":true,\"causes\":[]}";


}
