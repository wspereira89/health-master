package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Glassfish;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemoryGlassfishRepositoryImpl implements GlassfishRepository{
    @Override
    public Optional<Glassfish> getGlassfishByServerIdAndId(String serverId, String id) {
        return Optional.empty();
    }
}
