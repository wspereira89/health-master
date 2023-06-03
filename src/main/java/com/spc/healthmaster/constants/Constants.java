package com.spc.healthmaster.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {

    public static final String ALREADY_INITIALIZED_STATUS = "already_initialized";
    public static final String ALREADY_STOPPED_STATUS = "already_stopped";
    public static final String SSH_CONNECTION_STATUS = "ssh_connection";
    
    public static final String JAVA_HOME = "/usr/java/jdk1.8.0_74/bin/java" ;
    public static final String JPROFILER_OPTS = "-agentpath:%s/libjprofilerti.so";
    public static final String JMX_OPTS ="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%1$s -Djava.rmi.server.hostname=%2$s -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=%3$s/jmxremote.password -Dcom.sun.management.jmxremote.access.file=%4$s/jmxremote.access";;

}
