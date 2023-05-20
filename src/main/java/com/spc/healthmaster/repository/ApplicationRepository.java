package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.SSHManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<SSHManager, Long> {

}
