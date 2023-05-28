package com.spc.healthmaster.strategy;


import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringBootCommandStrategy extends BaseCommandStrategy implements CommandStrategy {
    private static final String COMMAND_START = "";
    private static final String COMMAND_STOP = "";
    private static final String COMMAND_STATUS = "";

    @Override
    public String start(final WrapperExecute wrapper) throws ApiException {
        //sshManagerDto.executeCommand(COMMAND_START);
        return "";
    }

    @Override
    public String stop(final WrapperExecute wrapper) throws ApiException {
       // sshManagerDto.executeCommand(COMMAND_STOP);
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
        return TypeStrategy.SPRING_BOOT_APP;
    }
}
