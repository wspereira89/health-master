package com.spc.healthmaster.constants.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SshManagerResponseConstants {

    public static final String SSHMANAGER_RESPONSE_ERROR_400_ARGUMENTS_NOT_VALID = "{\n" +
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



    public static final String ERROR_400_ALREADY_EXIST_SSHMANAGER = "{\n" +
            "    \"error\": \"already_exist_ssManager\",\n" +
            "    \"message\": \"the sshManager [SERVERNAME] is already registered in the database\",\n" +
            "    \"status\": 400,\n" +
            "    \"causes\": []\n" +
            "}";

    public static final String SSHMANAGER_ERROR_502_CONNECTION_SSH = "{\n" +
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
   }
