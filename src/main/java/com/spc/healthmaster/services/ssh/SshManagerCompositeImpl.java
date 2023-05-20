package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.notFoundConnectionSsh;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(value = SCOPE_SINGLETON)
public class SshManagerCompositeImpl implements SshManagerComposite {

    private Map<Long, SshManagerDto> sshManagerMap;

    private final SSHManagerRepository sshManagerRepository;

    public SshManagerCompositeImpl(
            final SSHManagerRepository sshManagerRepository, final  Map<Long, SshManagerDto> sshManagerDto
    ) {
     this.sshManagerRepository = sshManagerRepository;
     this.sshManagerMap = sshManagerDto;
    }

    @PostConstruct
    public void init() {
        this.sshManagerMap = sshManagerRepository.findAll()
                .stream()
                .collect(Collectors.toMap(SSHManager::getId, SSHManager::toSshManager));
    }

    public SshManagerDto getSshManagerMapById(final Long serverId) throws ApiException {
        return Optional
                .ofNullable(sshManagerMap.get(serverId))
                .orElseThrow(()-> notFoundConnectionSsh(serverId).toException());
    }

}
