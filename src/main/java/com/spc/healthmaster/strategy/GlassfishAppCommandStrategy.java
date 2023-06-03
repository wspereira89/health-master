package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_STOPPED;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;
import com.spc.healthmaster.repository.ApplicationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlassfishAppCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String ENABLED = "enabled";
   
    private static final String STOP_COMMAND ="/opt/glassfish4/bin/asadmin --user=admin --passwordfile=%1s$ --port %2$s disable %3$s";
    private static final String START_COMMAND="/opt/glassfish4/bin/asadmin --user=admin --passwordfile=%1s$ --port %2$s enable %3$s";
    private static final String STATUS_COMMAND ="/opt/glassfish4/bin/asadmin --user=admin --passwordfile=%1$s --port %2$s list-applications --long --type web | grep %3$s";

   
    
    @Override
    public void start(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager =wrapper.getSshManagerDto();
        if(this.status(wrapper)){
              throw ALREADY_STOPPED.toException();
        }
        final ServerManager  serverManager = wrapper.getServerManager();
        manager.executeCommand(String.format(START_COMMAND, serverManager.getPasswordPath(), serverManager.getPort(), wrapper.getApplication().getApplicationName()));
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager =wrapper.getSshManagerDto();
        if(!this.status(wrapper)){
              throw ALREADY_INITIALIZED.toException();
        }
         final ServerManager  serverManager = wrapper.getServerManager();
        manager.executeCommand(String.format(STOP_COMMAND, serverManager.getPasswordPath(), serverManager.getPort(),  wrapper.getApplication().getApplicationName()));
    }

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException {
        
        final ServerManager  serverManager = wrapper.getServerManager();
      
        //String passwordFile ="glassfish4-domains/appsdomain4/config/mypass.txt";
        final SshManagerDto manager =wrapper.getSshManagerDto();
        final String result =manager
                .executeCommand(String.format(STATUS_COMMAND, serverManager.getPasswordPath(), serverManager.getPort(),  wrapper.getApplication().getApplicationName()));
        if(result.isEmpty()){
            //Todo lanzar exception la aplicacion no se encuentra deployada en el glasfish
            throw ALREADY_STOPPED.toException();
        }
        return result.contains(ENABLED);
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
        return TypeStrategy.GLASSFISH_APP;
    }
}
