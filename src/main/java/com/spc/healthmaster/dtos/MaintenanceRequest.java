package com.spc.healthmaster.dtos;

import com.spc.healthmaster.enums.Actions;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MaintenanceRequest {

    @NotNull(message = "Invalid command action")
    private final Actions command;

    @NotBlank(message = "MaintenanceId is required")
    private final String maintenanceId;

}
