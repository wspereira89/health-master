package com.spc.healthmaster.dtos;

import com.spc.healthmaster.enums.Action;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MaintenanceRequestDto {

    @NotNull(message = "Invalid command action")
    private final Action command;

    @NotBlank(message = "MaintenanceId is required")
    private final String maintenanceId;

}
