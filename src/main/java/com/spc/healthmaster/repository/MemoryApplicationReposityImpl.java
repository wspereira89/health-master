package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Aplication;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemoryApplicationReposityImpl implements  ApplicationRepository{
    @Override
    public Optional<Aplication> getApplication(String id) {
        return Optional.empty();
    }
}
