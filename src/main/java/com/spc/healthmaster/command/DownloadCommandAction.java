package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

@Component(DownloadCommandAction.DOWNLOAD_COMMAND_ACTION)
public class DownloadCommandAction implements CommandAction {

    public static final String DOWNLOAD_COMMAND_ACTION = "downloadCommandAction";

    @Override
    public ResponseDto execute(final WrapperExecute wrapper) throws ApiException {
        return null;
    }
}