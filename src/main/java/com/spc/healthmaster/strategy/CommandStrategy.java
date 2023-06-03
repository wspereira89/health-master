package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface CommandStrategy {

    void start(WrapperExecute wrapper) throws  ApiException;
    
    void stop(WrapperExecute wrapper) throws  ApiException;
    
    boolean status(WrapperExecute wrapper) throws ApiException;

    List<FileDto> getListFile(WrapperExecute wrapper) throws ApiException;

    byte[] downloadFile(WrapperExecute wrapper) throws ApiException;

    TypeStrategy getType();
}
