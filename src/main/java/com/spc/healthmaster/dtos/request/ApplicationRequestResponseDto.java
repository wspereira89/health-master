package com.spc.healthmaster.dtos.request;

import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationRequestResponseDto {
    private Long id;
    private String applicationName;
    private String pathFile;
    private Integer jmxPort;
    private String memory;
    private Long serverManagerId;

    public Application toApplication(ServerManager serverManager) {
        return  new Application(id, applicationName, pathFile, jmxPort, memory, serverManager);
    }
}
