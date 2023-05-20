package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.ServerManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerManagerRepository extends JpaRepository<ServerManager, Long> {

}
