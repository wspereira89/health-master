package com.spc.healthmaster.dtos.request;

import com.spc.healthmaster.enums.Action;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CommandRequestDto {
    @NotNull(message = "Invalid command action")
    private final Action command;
    @NotNull(message = "Invalid type strategy")
    private final TypeStrategy typeStrategy;
    @Min(value = 1, message = "The SSH Manager ID must be greater than zero")
    @NotNull(message = "sshManagerId is required")
    private final Long sshManagerId;
    private final Long serverManagerId;
    private final Long applicationId;
    private final String nameFile;
    private final String pathFile;
}
