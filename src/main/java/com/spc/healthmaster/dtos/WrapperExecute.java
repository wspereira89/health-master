package com.spc.healthmaster.dtos;

import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.strategy.CommandStrategy;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WrapperExecute {
    private final CommandStrategy commandStrategy;
    private final SshManagerDto sshManagerDto;
    private final ServerManager serverManager;
    private final String pathFile;
    private final Application application;
}
