package com.spc.healthmaster.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SwaggerResponseConstants {

    public static final String RESPONSE_SUCCESS_RUNNING ="{\n" +
            " \"status\":\"RUNNING\",\n" +
            " \"fileDtoList\":\"null\",\n" +
            " \"download\":\"null\",\n" +
            "}";

    public static final String RESPONSE_SUCCESS_STOPPED ="{\n" +
            " \"status\":\"STOPPED\",\n" +
            " \"fileDtoList\":\"null\",\n" +
            " \"download\":\"null\",\n" +
            "}";
    public static final String RESPONSE_SUCCESS_UNDEFINED ="{\n" +
            " \"status\":\"UNDEFINED\",\n" +
            " \"fileDtoList\":\"null\",\n" +
            " \"download\":\"null\",\n" +
            "}";

    public static final String RESPONSE_SUCCESS_LOG ="{\n" +
            " \"status\":\"null\",\n" +
            "  \"fileDtoList\":[\n" +
            "    {\"name\":\"log1\",\"downloadUrl\":\"/logs/log1.txt\"},\n" +
            "    {\"name\":\"log2\",\"downloadUrl\":\"/logs/log2.txt\"},\n" +
            "  ], \n" +
            "\"download\":\"null\"\n" +
            "}";
    public static final String RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID = "{\n" +
            "  \"error\":\"validation_error\",\n" +
            "  \"message\":\"Arguments not valid\",\n" +
            "  \"status\":400,\n" +
            "  \"causes\":[\n" +
            "    {\"code\":\"typeStrategy\",\"description\":\"Invalid type strategy\"},\n" +
            "    {\"code\":\"command\",\"description\":\"Invalid command action\"},\n" +
            "    {\"code\":\"sshManagerId\",\"description\":\"sshManagerId is required\"},\n" +
            "    {\"code\":\"serverManagerId\",\"description\":\"ServerManagerId is required\"}\n" +
            "  ],\"expected\":false\n" +
            "}";

    public static final String ERROR_400_DESERIALIZATION_COMMAND = "{\n" +
            "  \"error\":\"deserialization_error\",\n" +
            "  \"message\":\"JSON deserialization error\",\n" +
            "  \"status\":400,\n" +
            "  \"causes\":[{\"code\":\"Action\",\"description\":\"must be one of: [START, STOP, STATUS, LOG, DOWNLOAD]\"}],\n" +
            "  \"expected\":false\n" +
            "}";

    public static final String ERROR_400_DESERIALIZATION_TYPE_STRATEGY = "{\n" +
            "  \"error\":\"deserialization_error\",\n" +
            "  \"message\":\"JSON deserialization error\",\n" +
            "  \"status\":400,\n" +
            "  \"causes\":[\n" +
            "    {\n" +
            "      \"code\":\"TypeStrategy\",\n" +
            "      \"description\":\"must be one of: [SPRING_BOOT_APP, GLASSFISH_SERVER, TOMCAT_SERVER, GLASSFISH_APP, TOMCAT_APP, ACTIVEMQ_APP, MONGODB_APP]\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"expected\":false\n" +
            "}\n";

    public static final String ERROR_400_DESERIALIZATION_UNKNOWN = "{\n" +
            "  \"error\":\"deserialization_error\",\n" +
            "  \"message\":\"JSON deserialization error\",\n" +
            "  \"status\":400,\n" +
            "  \"causes\":[{\"code\":\"unknown\",\"description\":\"must be one of\"}],\n" +
            "  \"expected\":false\n" +
            "}";
}
