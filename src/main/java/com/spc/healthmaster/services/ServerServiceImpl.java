package com.spc.healthmaster.services;

import com.spc.healthmaster.dtos.ServerDto;
import com.spc.healthmaster.entity.Server;
import com.spc.healthmaster.repository.SshManagerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServerServiceImpl implements ServerService {

    private final SshManagerRepository sshManagerRepository;

    public ServerServiceImpl(final SshManagerRepository sshManagerRepository) {
        this.sshManagerRepository = sshManagerRepository;
    }


    @Override
    public Optional<Server> create(ServerDto server) {

        return Optional.empty();
    }

    @Override
    public Optional<Server> edit(ServerDto server) {
        return Optional.empty();
    }

    @Override
    public boolean delete(String serverId) {
        return false;
    }
}
