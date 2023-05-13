package com.spc.healthmaster.strategy;


import com.jcraft.jsch.JSchException;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.ssh.dto.SshManagerDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringBootCommandStrategy implements CommandStrategy {
    private static final String COMMAND_START = "";
    private static final String COMMAND_STOP = "";
    private static final String COMMAND_STATUS = "";

    @Override
    public void start(final SshManagerDto sshManagerDto, final Aplication aplication) throws JSchException, IOException, IllegalAccessException {
        sshManagerDto.executeCommand(COMMAND_START);
    }

    @Override
    public void stop(final SshManagerDto sshManagerDto, final Aplication aplication) throws JSchException, IOException, IllegalAccessException {
        sshManagerDto.executeCommand(COMMAND_STOP);
    }

    @Override
    public boolean status(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return true;
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.SPRING_BOOT_APP;
    }
}
