package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Glassfish;

import java.util.Optional;

public interface GlassfishRepository {

    Optional<Glassfish> getGlassfishByServerIdAndId(String serverId, String id);
}
