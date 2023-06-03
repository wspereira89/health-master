package com.spc.healthmaster.strategy;


import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.spc.healthmaster.constants.Constants.JAVA_HOME;
import static com.spc.healthmaster.constants.Constants.JMX_OPTS;
import static com.spc.healthmaster.factories.ApiErrorFactory.alreadyInitializedException;

@Component
public class SpringBootCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String PROFILE = "/home/%s/profiler";

    @Override
    public void start(final WrapperExecute wrapper) throws ApiException {
         final SshManagerDto manager = wrapper.getSshManagerDto();

         if (this.status(wrapper)) {
              throw alreadyInitializedException(wrapper.getApplication().getApplicationName()).toException();
         }

         final Application application = wrapper.getApplication();
         final String jar = application.getPathFile() + "/" + application.getApplicationName() + ".jar";
         final String profile =String.format(PROFILE, manager.getUser());
         final String javaOptions=String.format(JMX_OPTS, application.getJmxPort(), manager.getHost(), profile, profile);
         final String command = JAVA_HOME + " "+ application.getMemory() +"" +javaOptions +" -jar -Dspring.profiles.active=dev " +jar +" > /dev/null &" ;
         manager.executeCommand(command);
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        super.kill(wrapper.getSshManagerDto(), wrapper.getApplication().getApplicationName());
    }
    
  

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException {
       return ! super.getPdi(wrapper.getSshManagerDto(), wrapper.getApplication().getApplicationName())
               .isEmpty();
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
