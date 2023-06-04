package com.spc.healthmaster.services.maintenance;

import com.spc.healthmaster.dtos.request.MaintenanceRequestDto;

public interface MaintenanceService {

    String executeMaintenance(MaintenanceRequestDto maintenanceRequestDto);
}
