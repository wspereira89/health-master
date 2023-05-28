package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_INITIALIZED;

@Component(StartCommandAction.START_COMMAND_ACTION)
public class StartCommandAction implements CommandAction {
    public static final String START_COMMAND_ACTION = "startCommandAction";

    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {
        final CommandStrategy commandStrategy =wrapper.getCommandStrategy();
        if (commandStrategy.status(wrapper)) {
           throw  ALREADY_INITIALIZED.toException();
        }
         commandStrategy.start(wrapper);
        return null;
    }
}
