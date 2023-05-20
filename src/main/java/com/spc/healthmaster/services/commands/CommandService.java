package com.spc.healthmaster.services.commands;

import com.spc.healthmaster.dtos.CommandRequestDto;
import com.spc.healthmaster.exception.ApiException;

public interface CommandService {


    String executeCommand(CommandRequestDto commandRequestDto) throws ApiException;
}
