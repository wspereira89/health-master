package com.spc.healthmaster.ssh;

import com.spc.healthmaster.ssh.dto.SshManagerDto;
import com.spc.healthmaster.ssh.entity.Server;
import com.spc.healthmaster.ssh.repository.SshManagerRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SshManagerComposite {

    private Map<String, SshManagerDto> sshManagerMap;

    private final SshManagerRepository sshManagerRepository;

    public SshManagerComposite(final SshManagerRepository sshManagerRepository) {
     this.sshManagerRepository = sshManagerRepository;
     this.sshManagerMap = new ConcurrentHashMap<>();
    }


    public void addServer() {
        final List<Server> servers = sshManagerRepository.getAllServer();
        this.sshManagerMap = servers.stream().collect(Collectors.toMap(Server::getServerId, Server::toSshManager));
    }

    public Map<String, SshManagerDto> getSshManagerMap() {
        return this.sshManagerMap;
    }

}
