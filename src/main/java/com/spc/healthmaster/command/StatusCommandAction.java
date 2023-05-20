package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

@Component(StatusCommandAction.STATUS_COMMAND_ACTION)
public class StatusCommandAction implements CommandAction {

    public static final String STATUS_COMMAND_ACTION = "statusCommandAction";

    @Override
    public String execute(
            final CommandStrategy commandStrategy, final SshManagerDto manager, final Aplication application
    ) throws ApiException {
        return commandStrategy.status(manager, application)
                ? "The application is initialized"
                : "The application is stopped";
    }
}
