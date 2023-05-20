package com.spc.healthmaster.dtos;

import com.spc.healthmaster.enums.Actions;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommandRequestDto {
    @NotNull(message = "Invalid command action")
    private final Actions command;
    @NotNull(message = "Invalid type strategy")
    private final TypeStrategy typeStrategy;
    @NotBlank(message = "Server ID is required")
    private final String serverId;
    @NotBlank(message = "Application is required")
    private final String application;
}
