package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;

public interface CommandAction {

    ResponseDto execute(final WrapperExecute wrapper) throws ApiException;

}
