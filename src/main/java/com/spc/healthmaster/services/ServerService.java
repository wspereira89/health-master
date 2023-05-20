package com.spc.healthmaster.services;

import com.spc.healthmaster.dtos.ServerDto;
import com.spc.healthmaster.entity.Server;

import java.util.Optional;

public interface ServerService {

    Optional<Server>  create(ServerDto server);
    Optional<Server>  edit(ServerDto server);
    boolean delete(String serverId);

}
