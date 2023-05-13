package com.spc.healthmaster.strategy;
import com.jcraft.jsch.JSchException;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.repository.GlassfishRepository;
import com.spc.healthmaster.ssh.dto.SshManagerDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GlassfishServerCommandStrategy implements CommandStrategy {

    private static final String COMMAND_STOP = "asadmin --user admin --passwordfile /home/user/password.txt stop-instance --sync instance1";
    private static final String COMMAND_START ="asadmin start-instance --sync instance1 --user admin --passwordfile /home/user/password.txt";
    private static final String COMMAND_STATUS = "asadmin list-instances --user admin --passwordfile /home/user/password.txt | grep running-instance  | grep instance_name";
    private final GlassfishRepository glassfishRepository;

    public GlassfishServerCommandStrategy(final GlassfishRepository glassfishRepository) {
        this.glassfishRepository = glassfishRepository;
    }

    @Override
    public void start(final SshManagerDto sshManagerDto, final Aplication aplication) {


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
        return TypeStrategy.GLASSFISH_SERVER;
    }
}
