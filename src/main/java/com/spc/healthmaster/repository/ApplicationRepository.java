package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByServerManagerId(Long serverManagerId);

    Optional<Application> findByApplicationNameAndServerManagerId(String name, Long serverManagerId);

}
