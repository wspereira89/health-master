package com.spc.healthmaster.constants.swagger;

public class ApplicationResponseConstant {

    public static final String RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID = "{\n" +
            "    \"error\": \"validation_error\",\n" +
            "    \"message\": \"Arguments not valid\",\n" +
            "    \"status\": 400,\n" +
            "    \"causes\": [\n" +
            "        {\n" +
            "            \"code\": \"applicationName\",\n" +
            "            \"description\": \"Invalid applicationName\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"code\": \"pathFile\",\n" +
            "            \"description\": \"Invalid pathFile\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"code\": \"memory\",\n" +
            "            \"description\": \"Invalid memory\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"code\": \"jmxPort\",\n" +
            "            \"description\": \"Invalid jmxPort\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String ERROR_400_ALREADY_EXIST_APPLICATION = "{\n" +
            "    \"error\": \"already_exist_application\",\n" +
            "    \"message\": \"the APPLICATION [SERVERNAME] is already registered in the database\",\n" +
            "    \"status\": 400,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String ERROR_404_NOT_FOUND_APPLICATION = "{\n" +
            "    \"error\": \"not_found_application\",\n" +
            "    \"message\": \"not found server application: 1\",\n" +
            "    \"status\": 404,\n" +
            "    \"causes\": []\n" +
            "}";
}
