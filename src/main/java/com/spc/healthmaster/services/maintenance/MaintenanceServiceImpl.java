package com.spc.healthmaster.services.maintenance;

import com.spc.healthmaster.dtos.request.MaintenanceRequestDto;
import com.spc.healthmaster.event.MaintenanceEventPublisher;
import com.spc.healthmaster.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceEventPublisher eventPublisher;

    public MaintenanceServiceImpl(
            final MaintenanceRepository maintenanceRepository,
            final MaintenanceEventPublisher eventPublisher
    ) {
        this.maintenanceRepository = maintenanceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String executeMaintenance(MaintenanceRequestDto maintenanceRequestDto) {
        eventPublisher.publishEvent("");


        return "HOLA";
    }
}
