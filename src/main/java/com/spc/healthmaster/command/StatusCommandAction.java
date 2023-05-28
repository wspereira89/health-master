package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.stereotype.Component;

@Component(StatusCommandAction.STATUS_COMMAND_ACTION)
public class StatusCommandAction implements CommandAction {

    public static final String STATUS_COMMAND_ACTION = "statusCommandAction";

    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {
        final CommandStrategy commandStrategy =wrapper.getCommandStrategy();
        return ResponseDto.builder()
                .status(
                        commandStrategy.status(wrapper)
                                ? Status.RUNNING
                                : Status.STOPPED
                )
                .build();
    }
}
