package com.spc.healthmaster.strategy;


import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

@Component
public class SpringBootCommandStrategy implements CommandStrategy {
    private static final String COMMAND_START = "";
    private static final String COMMAND_STOP = "";
    private static final String COMMAND_STATUS = "";

    @Override
    public String start(final SshManagerDto sshManagerDto, final ServerManager serverManager) throws ApiException {
        sshManagerDto.executeCommand(COMMAND_START);
        return "";
    }

    @Override
    public String stop(final SshManagerDto sshManagerDto, final ServerManager serverManager) throws ApiException {
        sshManagerDto.executeCommand(COMMAND_STOP);
        return "";
    }

    @Override
    public boolean status(final SshManagerDto sshManagerDto, final ServerManager serverManager) {
        return true;
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.SPRING_BOOT_APP;
    }
}
