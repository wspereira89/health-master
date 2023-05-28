package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TomcatAppCommandStrategy extends BaseCommandStrategy implements CommandStrategy {


    @Override
    public String start(WrapperExecute wrapper) throws ApiException {
        return null;
    }

    @Override
    public String stop(WrapperExecute wrapper) throws ApiException {
        return null;
    }

    @Override
    public boolean status(WrapperExecute wrapper) throws ApiException {
        return false;
    }

    @Override
    public List<FileDto> getListFile(final WrapperExecute wrapper) throws ApiException {
        return super.getListFile(wrapper);
    }

    @Override
    public byte[] downloadFile(final WrapperExecute wrapper) throws ApiException {
        return super.downloadFile(wrapper);
    }

    @Override
    public TypeStrategy getType() {
        return TypeStrategy.TOMCAT_APP;
    }
}
