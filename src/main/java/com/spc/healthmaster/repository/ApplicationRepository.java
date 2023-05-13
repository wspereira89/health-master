package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Aplication;

import java.util.Optional;

public interface ApplicationRepository {

    Optional<Aplication> getApplication(String id);
}
