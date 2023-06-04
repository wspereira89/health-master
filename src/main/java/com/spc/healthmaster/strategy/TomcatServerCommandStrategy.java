package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.request.response.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.spc.healthmaster.factories.ApiErrorFactory.alreadyInitializedException;

@Component
public class TomcatServerCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String START_COMMAND = "/home/%s/%s/tomcat/bin/tomcat start";
    
    @Override
    public void start(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager = wrapper.getSshManagerDto();

        if (status(wrapper)) {
              throw alreadyInitializedException(wrapper.getServerManager().getUsername()).toException();
        }

        manager.executeCommand(String.format(START_COMMAND, manager.getUser(), wrapper.getServerManager().getUsername()));
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        super.kill(wrapper.getSshManagerDto(), wrapper.getServerManager().getUsername());
    }

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException {
       return ! super.getPdi(wrapper.getSshManagerDto(), wrapper.getServerManager().getUsername()).isEmpty();
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
        return TypeStrategy.TOMCAT_SERVER;
    }
}
