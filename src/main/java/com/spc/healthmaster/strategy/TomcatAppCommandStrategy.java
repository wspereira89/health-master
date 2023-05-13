package com.spc.healthmaster.strategy;

import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.ssh.dto.SshManagerDto;
import org.springframework.stereotype.Component;

@Component
public class TomcatAppCommandStrategy implements CommandStrategy{


    @Override
    public void start(final SshManagerDto sshManagerDto, final Aplication aplication) {

    }

    @Override
    public void stop(final SshManagerDto sshManagerDto, final Aplication aplication) {

    }

    @Override
    public boolean status(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return true;
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.TOMCAT_APP;
    }
}
