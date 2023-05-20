package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Maintenance;

import java.util.Optional;

public interface MaintenanceRepository {

    Optional<Maintenance> findById(String maintenanceId);
}
