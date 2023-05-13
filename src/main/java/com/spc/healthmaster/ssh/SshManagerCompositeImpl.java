package com.spc.healthmaster.ssh;

import com.spc.healthmaster.ssh.dto.SshManagerDto;
import com.spc.healthmaster.ssh.entity.Server;
import com.spc.healthmaster.ssh.repository.SshManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(value = SCOPE_SINGLETON)
public class SshManagerCompositeImpl implements SshManagerComposite{

    private Map<String, SshManagerDto> sshManagerMap;

    private final SshManagerRepository sshManagerRepository;

    public SshManagerCompositeImpl(
            final SshManagerRepository sshManagerRepository, final  Map<String, SshManagerDto> sshManagerDto
    ) {
     this.sshManagerRepository = sshManagerRepository;
     this.sshManagerMap = sshManagerDto;
    }


    public void addServer() {
        final List<Server> servers = sshManagerRepository.getAllServer();
        this.sshManagerMap = servers.stream().collect(Collectors.toMap(Server::getServerId, Server::toSshManager));
    }

    public SshManagerDto getSshManagerMapById(final String serverId) throws Exception {
        return Optional.ofNullable(sshManagerMap.get(serverId)).orElseThrow(()-> new Exception("not found"));
    }

}
