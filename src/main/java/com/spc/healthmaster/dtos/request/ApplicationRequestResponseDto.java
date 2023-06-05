package com.spc.healthmaster.dtos.request;

import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationRequestResponseDto {
    private Long id;
    @NotNull(message = "Invalid applicationName")
    private String applicationName;
    @NotNull(message = "Invalid pathFile")
    private String pathFile;
    @NotNull(message = "Invalid jmxPort")
    private Integer jmxPort;
    @NotNull(message = "Invalid memory")
    private String memory;
    private long serverManagerId;

    public Application toApplication(ServerManager serverManager) {
        return  new Application(id, applicationName, pathFile, jmxPort, memory, serverManager);
    }
}
