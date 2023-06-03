package com.spc.healthmaster.strategy;

import com.spc.healthmaster.dtos.FileDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import static com.spc.healthmaster.factories.ApiErrorFactory.ALREADY_STOPPED;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseCommandStrategy {
    
    private static final String COMMAND_STATUS = "ps -ef | grep %s | grep -v grep | awk '{print $2}'";
    private static final String COMMAND_STOP = "kill -9 -f %s";
    
    protected  void kill(final SshManagerDto manager, final String application) throws ApiException {
        final String psId = this.getPdi(manager, application);
        if(psId.isEmpty()){
              throw ALREADY_STOPPED.toException();
        }
        
       manager.executeCommand(String.format(COMMAND_STOP, psId));
    }
    
    protected  String getPdi(final SshManagerDto manager, final String application) throws ApiException {
        return manager.executeCommand(String.format(COMMAND_STATUS, application));
    }
    
    protected List<FileDto> getListFile(final WrapperExecute wrapper) throws ApiException {
       //Todo toca buscar la aplicacion o server
        // Ejecutar el comando para obtener la lista de archivos TXT en la carpeta
        String command = "ls " + "/ruta/de/la/carpeta/" + "*.txt";
        String result = wrapper.getSshManagerDto().executeCommand(command);
        // Dividir el resultado por saltos de línea para obtener una lista de archivos
        String[] fileNames = result.split("\\r?\\n");
        return Arrays.stream(fileNames)
                .map(fileName -> new FileDto(fileName, "/files/download/" + fileName))
                .collect(Collectors.toList());
    }

    protected byte[] downloadFile(final WrapperExecute wrapper) throws ApiException {
        return wrapper.getSshManagerDto().downloadFile(wrapper.getPathFile());

    }
}
