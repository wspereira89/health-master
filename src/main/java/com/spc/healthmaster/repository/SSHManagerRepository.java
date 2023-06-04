package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.SSHManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SSHManagerRepository  extends JpaRepository<SSHManager, Long> {

    Optional<SSHManager> findByServerNameAndHostAndUserName(String serverName, String host, String username);
}
