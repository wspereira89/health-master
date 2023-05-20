package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;

public interface CommandStrategy {

    String start(SshManagerDto sshManagerDto,  Aplication aplication) throws  ApiException;
    String stop(SshManagerDto sshManagerDto, Aplication aplication) throws  ApiException;
    boolean status(SshManagerDto sshManagerDto, Aplication aplication) throws ApiException;

    TypeStrategy getType();
}
