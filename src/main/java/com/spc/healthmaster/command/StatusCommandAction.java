package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

@Component(StatusCommandAction.STATUS_COMMAND_ACTION)
public class StatusCommandAction implements CommandAction {

    public static final String STATUS_COMMAND_ACTION = "statusCommandAction";

    @Override
    public Status execute(
            final CommandStrategy commandStrategy, final SshManagerDto manager, final ServerManager serverManager
    ) throws ApiException {
        return commandStrategy.status(manager, serverManager)
                ? Status.RUNNING
                : Status.STOPPED;
    }
}
