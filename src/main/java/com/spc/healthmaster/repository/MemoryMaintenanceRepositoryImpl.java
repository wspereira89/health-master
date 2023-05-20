package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Maintenance;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemoryMaintenanceRepositoryImpl implements MaintenanceRepository {

    @Override
    public Optional<Maintenance> findById(String maintenanceId) {
        return Optional.empty();
    }
}
