package com.spc.healthmaster.services.maintenance;

import com.spc.healthmaster.dtos.MaintenanceRequestDto;

public interface MaintenanceService {

    String executeMaintenance(MaintenanceRequestDto maintenanceRequestDto);
}
