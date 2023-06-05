package com.spc.healthmaster.constants.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandRequestConstant {

    public static final String COMMAND_DESRIPTION_REQUEST ="A continuación se listan los ejemplos de los casos validos de como se tienen que enviar la información.\n" +
            " los command Validos son: [START, STOP, STATUS, LOG, DOWNLOAD] \n" +
            "Y las estrategias validas que ejecutaran los comanado son :" +
            "[SPRING_BOOT_APP, GLASSFISH_SERVER, TOMCAT_SERVER, GLASSFISH_APP, TOMCAT_APP, ACTIVEMQ_APP, MONGODB_APP].";
    public static final String REQUEST_START_VALID = "{\n" +
            "  \"command\":\"START\",\n" +
            "  \"typeStrategy\":\"SPRING_BOOT_APP\",\n" +
            "  \"sshManagerId\":1,\n" +
            "  \"serverManagerId\":1,\n" +
            "  \"applicationId\":1\n" +
            "}";
    public static final String REQUEST_STOP_VALID = "{\n" +
            "  \"command\":\"STOP\",\n" +
            "  \"typeStrategy\":\"SPRING_BOOT_APP\",\n" +
            "  \"sshManagerId\":1,\n" +
            "  \"serverManagerId\":1,\n" +
            "  \"applicationId\":1\n" +
            "}";
    public static final String REQUEST_STATUS_VALID ="{\n" +
            "  \"command\":\"STATUS\",\n" +
            "  \"typeStrategy\":\"SPRING_BOOT_APP\",\n" +
            "  \"sshManagerId\":1,\n" +
            "  \"serverManagerId\":1,\n" +
            "  \"applicationId\":1\n" +
            "}";
    public static final String REQUEST_LOG_VALID = "{\n" +
            "  \"command\":\"LOG\",\n" +
            "  \"typeStrategy\":\"SPRING_BOOT_APP\",\n" +
            "  \"sshManagerId\":1,\n" +
            "  \"serverManagerId\":1,\n" +
            "  \"applicationId\":1\n" +
            "}";

    public static final String REQUEST_DOWNLOAD_VALID = "{\n" +
            "  \"command\":\"DOWNLOAD\",\n" +
            "  \"typeStrategy\":\"SPRING_BOOT_APP\",\n" +
            "  \"sshManagerId\":1,\n" +
            "  \"serverManagerId\":1,\n" +
            "  \"applicationId\":1,\n" +
            "  \"nameFile\":\"geoserver-log\",\n" +
            "  \"pathFile\":\"/log/geoserver-log.txt\"\n" +
            "}";

}
