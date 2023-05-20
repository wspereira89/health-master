package com.spc.healthmaster.repository;

import com.spc.healthmaster.entity.Server;

import java.util.List;
import java.util.Optional;

public interface SshManagerRepository {

    List<Server> getAllServer();
    Optional<Server> getOne(String serverId);
    Server add(Server server);
    void delete(String serverId);
}
