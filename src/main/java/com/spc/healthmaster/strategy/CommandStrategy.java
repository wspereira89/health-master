package com.spc.healthmaster.strategy;

import com.jcraft.jsch.JSchException;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.ssh.dto.SshManagerDto;

import java.io.IOException;

public interface CommandStrategy {

    void start(SshManagerDto sshManagerDto,  Aplication aplication) throws JSchException, IOException, IllegalAccessException;
    void stop(SshManagerDto sshManagerDto, Aplication aplication) throws JSchException, IOException, IllegalAccessException;
    boolean status(SshManagerDto sshManagerDto, Aplication aplication);

    TypeStrategy getType();
}
