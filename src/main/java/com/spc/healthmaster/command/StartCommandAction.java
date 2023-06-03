package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;


@Component(StartCommandAction.START_COMMAND_ACTION)
public class StartCommandAction implements CommandAction {
    public static final String START_COMMAND_ACTION = "startCommandAction";

    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {
        final CommandStrategy commandStrategy =wrapper.getCommandStrategy();
        commandStrategy.start(wrapper);
        return null;
    }
}
