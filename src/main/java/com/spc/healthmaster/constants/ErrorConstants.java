package com.spc.healthmaster.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorConstants {

    public static final String ERROR ="{\n" +
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

}
