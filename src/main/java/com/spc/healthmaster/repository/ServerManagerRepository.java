package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerManagerRepository extends JpaRepository<ServerManager, Long> {
    List<ServerManager> findAllBySshManagerId(Long sshManagerId);
    Optional<ServerManager> findAllByTypeStrategyAndUsernameAndSshManagerId(TypeStrategy typeStrategy, String username, Long sshManagerId);
}
