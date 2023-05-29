package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.SSHManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SSHManagerRepository  extends JpaRepository<SSHManager, Long> {

    Optional<SSHManager> findByServerNameAndHostAndUsername(String serverName, String host, String username);
}
