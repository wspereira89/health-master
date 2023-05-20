package com.spc.healthmaster.services.maintenance;

import com.spc.healthmaster.dtos.MaintenanceRequest;
import com.spc.healthmaster.repository.MaintenanceRepository;
import com.spc.healthmaster.services.ssh.SshManagerComposite;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    private final SshManagerComposite sshManagerComposite;
    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceServiceImpl(
            final SshManagerComposite sshManagerComposite,
            final MaintenanceRepository maintenanceRepository
    ) {
        this.sshManagerComposite = sshManagerComposite;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public String executeMaintenance(MaintenanceRequest maintenanceRequest) {


        return null;
    }
}
