package com.spc.healthmaster.strategy;


import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_STOPPED;
import static com.spc.healthmaster.constants.Constants.JAVA_HOME;
import static com.spc.healthmaster.constants.Constants.JPROFILER_OPTS;
import static com.spc.healthmaster.constants.Constants.JMX_OPTS;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringBootCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String COMMAND_START = "";

    @Override
    public void start(final WrapperExecute wrapper) throws ApiException {
         final SshManagerDto manager =wrapper.getSshManagerDto();
         if(this.status(wrapper)){
              throw ALREADY_STOPPED.toException();
         }
        String user ="usrprocesos";
        String path = String.format("/home/%s/sacintmtr/electronic-seals",user);
        String jar = path +"/electronic-seals.jar";
        String profile =String.format("/home/%s/profiler",user);
        String jProfileOpt = String.format(JPROFILER_OPTS, profile);
        String jmxPort ="14899";
        String host ="10.18.100.30";
        String javaOptions=String.format(JMX_OPTS, jmxPort, host, profile, profile);        
        String java_opts =String.format("-Xms128m -Xmx256m %s",javaOptions);
        String command = JAVA_HOME + " "+ java_opts +" -jar -Dspring.profiles.active=dev " +jar +" > /dev/null &" ;
        System.out.println( manager.executeCommand(command));
       
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager =wrapper.getSshManagerDto();
        super.kill(manager, "electronic-seals");
    }
    
  

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException {
       final String result = super.getPdi(wrapper.getSshManagerDto(), "electronic-seals");
       return !result.isEmpty();
    }

    @Override
    public List<FileDto> getListFile(final WrapperExecute wrapper) throws ApiException {
        return super.getListFile(wrapper);
    }

    @Override
    public byte[] downloadFile(final WrapperExecute wrapper) throws ApiException {
        return super.downloadFile(wrapper);
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.SPRING_BOOT_APP;
    }
}
