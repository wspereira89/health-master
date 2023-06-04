package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.request.response.FileDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoDBCommandStrategy extends BaseCommandStrategy implements CommandStrategy {

    @Override
    public void start(final WrapperExecute wrapper) {
        
    }

    @Override
    public void stop(final WrapperExecute wrapper) {
       
    }

    @Override
    public boolean status(final WrapperExecute wrapper) {
        return true;
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
        return TypeStrategy.MONGODB_APP;
    }
}
