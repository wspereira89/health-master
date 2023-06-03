package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;


@Component(StopCommandAction.STOP_COMMAND_ACTION)
public class StopCommandAction implements CommandAction  {


    public static final String STOP_COMMAND_ACTION = "stopCommandAction";

    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {

        final CommandStrategy commandStrategy = wrapper.getCommandStrategy();
         commandStrategy.stop(wrapper);
        return null;
    }
}
