package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_STOPPED;
import com.spc.healthmaster.repository.ApplicationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TomcatAppCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private final String RUNNING = "running";
    private final String START_COMMAND= "curl --user %1s$:%2s$ http://%3s$:%4s$//manager/text/start?path=/%5s$";
    private final String STOP_COMMAND= "curl --user %1s$:%2s$ http://%3s$:%4s$//manager/text/stop?path=/%5s$";
    private final String STATUS_COMMAND= "curl --user %1s$:%2s$ http://%3s$:%4s$//manager/text/list | grep '/%5s$' ";
    

    
    @Override
    public void start(WrapperExecute wrapper) throws ApiException {
        
        
        final SshManagerDto manager =wrapper.getSshManagerDto();
         if(status(wrapper)){
              throw ALREADY_STOPPED.toException();
        }
        final ServerManager  serverManager = wrapper.getServerManager();
        final String command =String.format(START_COMMAND, 
                serverManager.getUsername(), 
                serverManager.getPasswordPath(),
                manager.getHost(),
                serverManager.getPort(),
                 wrapper.getApplication().getApplicationName());
         manager.executeCommand(command);
    }

    @Override
    public void stop(WrapperExecute wrapper) throws ApiException {
        
        final SshManagerDto manager =wrapper.getSshManagerDto();
        if(!status(wrapper)){
              throw ALREADY_STOPPED.toException();
        }
        final ServerManager  serverManager = wrapper.getServerManager();
        final String command =String.format(STOP_COMMAND, 
                serverManager.getUsername(), 
                serverManager.getPasswordPath(),
                manager.getHost(),
                serverManager.getPort(),
                wrapper.getApplication().getApplicationName());
        manager.executeCommand(command);
    }

    @Override
    public boolean status(WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager =wrapper.getSshManagerDto();
        final ServerManager  serverManager = wrapper.getServerManager();
        final String command =String.format(STATUS_COMMAND, 
                serverManager.getUsername(), 
                serverManager.getPasswordPath(),
                manager.getHost(),
                serverManager.getPort(),
                wrapper.getApplication().getApplicationName());
        return manager.executeCommand(command).contains(RUNNING);
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
        return TypeStrategy.TOMCAT_APP;
    }
}
