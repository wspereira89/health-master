package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_STOPPED;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlassfishServerCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String START_COMMAND ="script/%s start";
    
    @Override
    public void start(final WrapperExecute wrapper) throws ApiException {
        
        final SshManagerDto manager =wrapper.getSshManagerDto();
        
        if(this.status(wrapper)){
              throw ALREADY_INITIALIZED.toException();
        }
        
        manager
            .executeCommand(String.format(START_COMMAND, wrapper.getServerManager().getUsername()));
    }

    @Override
    public void stop(final WrapperExecute wrapper) throws ApiException {
        final SshManagerDto manager =wrapper.getSshManagerDto();
         super.kill(manager, wrapper.getServerManager().getUsername());
    }

    @Override
    public boolean status(final WrapperExecute wrapper) throws ApiException  {
       final String result = super
               .getPdi(wrapper.getSshManagerDto(), wrapper.getServerManager().getUsername());
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
        return TypeStrategy.GLASSFISH_SERVER;
    }
}
