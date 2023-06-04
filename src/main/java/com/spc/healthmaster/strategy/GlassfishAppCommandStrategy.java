package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.request.response.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;

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
              throw alreadyInitializedException(wrapper.getApplication().getApplicationName()).toException();
        }

        final ServerManager serverManager = wrapper.getServerManager();
        final String command = String.format(
                START_COMMAND,
                serverManager.getPassword(),
                serverManager.getPort(),
                wrapper.getApplication().getApplicationName()
        );
        manager.executeCommand(command);
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager = wrapper.getSshManagerDto();
        if (!this.status(wrapper)) {
              throw alreadyStoppedException(wrapper.getApplication().getApplicationName()).toException();
        }

        final ServerManager serverManager = wrapper.getServerManager();
        final String command = String.format(
                STOP_COMMAND,
                serverManager.getPassword(),
                serverManager.getPort(),
                wrapper.getApplication().getApplicationName()
        );
        manager.executeCommand(command);
    }

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException {
        
        final ServerManager serverManager = wrapper.getServerManager();
        final SshManagerDto manager = wrapper.getSshManagerDto();
        final Application application = wrapper.getApplication();
        final String command = String.format(
                STATUS_COMMAND,
                serverManager.getPassword(),
                serverManager.getPort(),
                application.getApplicationName()
        );

        final String result = manager.executeCommand(command);
        if (result.isEmpty()) {
            throw notDeployException(application.getApplicationName(), serverManager.getUsername()).toException();
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
