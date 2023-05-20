package com.spc.healthmaster.controller;

import com.spc.healthmaster.dtos.MaintenanceRequestDto;
import com.spc.healthmaster.services.maintenance.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(final MaintenanceService maintenanceService){
        this.maintenanceService = maintenanceService;
    }

    @PostMapping()
    public ResponseEntity<String> executeMaintenance(@Valid @RequestBody MaintenanceRequestDto maintenanceRequestDto) {
        return ResponseEntity
                .ok(this.maintenanceService.executeMaintenance(maintenanceRequestDto));
    }
}
