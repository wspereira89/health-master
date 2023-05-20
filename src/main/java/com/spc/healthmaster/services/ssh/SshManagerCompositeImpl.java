package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.Server;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SshManagerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.notFoundConnectionSsh;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(value = SCOPE_SINGLETON)
public class SshManagerCompositeImpl implements SshManagerComposite {

    private Map<String, SshManagerDto> sshManagerMap;

    private final SshManagerRepository sshManagerRepository;

    public SshManagerCompositeImpl(
            final SshManagerRepository sshManagerRepository, final  Map<String, SshManagerDto> sshManagerDto
    ) {
     this.sshManagerRepository = sshManagerRepository;
     this.sshManagerMap = sshManagerDto;
    }

    @PostConstruct
    public void init() {
        final List<Server> servers = sshManagerRepository.getAllServer();
        this.sshManagerMap = servers.stream().collect(Collectors.toMap(Server::getServerId, Server::toSshManager));
    }

    public SshManagerDto getSshManagerMapById(final String serverId) throws ApiException {
        return Optional
                .ofNullable(sshManagerMap.get(serverId))
                .orElseThrow(()-> notFoundConnectionSsh(serverId).toException());
    }

}
