package com.spc.healthmaster.services.maintenance;

import com.spc.healthmaster.dtos.MaintenanceRequest;

public interface MaintenanceService {

    String executeMaintenance(MaintenanceRequest maintenanceRequest);
}
