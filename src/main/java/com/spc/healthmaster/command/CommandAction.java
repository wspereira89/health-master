package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;

public interface CommandAction {

    Status execute(CommandStrategy commandStrategy, SshManagerDto manager, ServerManager serverManager) throws ApiException;

}
