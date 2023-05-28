package com.spc.healthmaster.services.commands;

import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.dtos.ResponseDto;
import com.spc.healthmaster.exception.ApiException;

public interface CommandService {


    ResponseDto executeCommand(CommandRequestDto commandRequestDto) throws ApiException;
}
