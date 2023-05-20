package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;

@Component(StartCommandAction.START_COMMAND_ACTION)
public class StartCommandAction implements CommandAction {
    public static final String START_COMMAND_ACTION = "startCommandAction";

    @Override
    public String execute(
            final CommandStrategy commandStrategy, final SshManagerDto manager, final Aplication application
    ) throws ApiException {
        if (commandStrategy.status(manager, application)) {
           throw  ALREADY_INITIALIZED.toException();
        }
        return commandStrategy.start(manager, application);
    }
}
