package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;

public interface CommandStrategy {

    String start(SshManagerDto sshManagerDto,  ServerManager serverManager) throws  ApiException;
    String stop(SshManagerDto sshManagerDto, ServerManager serverManager) throws  ApiException;
    boolean status(SshManagerDto sshManagerDto, ServerManager serverManager) throws ApiException;

    TypeStrategy getType();
}
