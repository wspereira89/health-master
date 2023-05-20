package com.spc.healthmaster.command;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Aplication;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;

public interface CommandAction {

    String execute(CommandStrategy commandStrategy, SshManagerDto manager, Aplication application) throws ApiException;

}
