package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;

@Component(StartCommandAction.START_COMMAND_ACTION)
public class StartCommandAction implements CommandAction {
    public static final String START_COMMAND_ACTION = "startCommandAction";

    @Override
    public Status execute(
            final CommandStrategy commandStrategy, final SshManagerDto manager, final ServerManager serverManager
    ) throws ApiException {
        if (commandStrategy.status(manager, serverManager)) {
           throw  ALREADY_INITIALIZED.toException();
        }
         commandStrategy.start(manager, serverManager);
        return null;
    }
}
