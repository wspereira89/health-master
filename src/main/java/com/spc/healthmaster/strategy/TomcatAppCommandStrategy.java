package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import org.springframework.stereotype.Component;

@Component
public class TomcatAppCommandStrategy implements CommandStrategy{


    @Override
    public String start(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return "";
    }

    @Override
    public String stop(final SshManagerDto sshManagerDto, final Aplication aplication) {
        return "";
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
