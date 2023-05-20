package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.GlassfishRepository;
import org.springframework.stereotype.Component;

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
    public String start(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return "";

    }

    @Override
    public String stop(final SshManagerDto sshManagerDto, final Aplication aplication) throws ApiException {
    sshManagerDto.executeCommand(COMMAND_STOP);
        return "";
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
