package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

@Component(LogCommandAction.LOG_COMMAND_ACTION)
public class LogCommandAction implements CommandAction {

    public static final String LOG_COMMAND_ACTION = "logCommandAction";
    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {
        return null;
    }
}
