package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlassfishServerCommandStrategy extends BaseCommandStrategy implements CommandStrategy {

    private static final String COMMAND_STOP = "asadmin --user admin --passwordfile /home/user/password.txt stop-instance --sync instance1";
    private static final String COMMAND_START ="asadmin start-instance --sync instance1 --user admin --passwordfile /home/user/password.txt";
    private static final String COMMAND_STATUS = "asadmin list-instances --user admin --passwordfile /home/user/password.txt | grep running-instance  | grep instance_name";


    public GlassfishServerCommandStrategy() {
    }

    @Override
    public String start(final WrapperExecute wrapper) {
        return "";

    }

    @Override
    public String stop(final WrapperExecute wrapper) throws ApiException {
    //sshManagerDto.executeCommand(COMMAND_STOP);
        return "";
    }

    @Override
    public boolean status(final WrapperExecute wrapper) {
        return true;
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
