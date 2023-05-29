package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.alreadyExistServer;
import static com.spc.healthmaster.factories.ApiErrorFactory.notFoundConnectionSsh;

@Service
public class SshManagerServiceImpl implements SshManagerService {
    private final SSHManagerRepository sshManagerRepository;
    private final Map<Long, SshManagerDto> sshManagerMap;

    public SshManagerServiceImpl(
            final SSHManagerRepository sshManagerRepository,
            final Map<Long, SshManagerDto> sshManagerMap
    ) {
        this.sshManagerRepository = sshManagerRepository;
        this.sshManagerMap =sshManagerMap;
    }

    @Override
    public List<SshManagerDto> getListSshManager() {
        return sshManagerRepository.findAll()
                .stream()
                .map(SSHManager::toSshManager)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteShhManager(final Long id) {
        //Todo pedir al señor orlando que las bd tengan constraint no se elimine si es usada en otro lado el id
        sshManagerRepository.deleteById(id);
        this.sshManagerMap.remove(id);
    }

    @Override
    public void saved(final SshManagerDto sshManagerDto) throws ApiException {
        final boolean existSsh = sshManagerRepository
                .findByServerNameAndHostAndUsername(sshManagerDto.getServerName(), sshManagerDto.getHost(), sshManagerDto.getUser())
                .isPresent();
        if(existSsh) {
            throw alreadyExistServer().toException();
        }

        modifySshManagerMap(sshManagerDto);
    }

    @Override
    public void edit(SshManagerDto sshManagerDto) throws ApiException {
        sshManagerRepository
                .findByServerNameAndHostAndUsername(sshManagerDto.getServerName(), sshManagerDto.getHost(), sshManagerDto.getUser())
                .orElseThrow(()->notFoundConnectionSsh(1l).toException());
        modifySshManagerMap(sshManagerDto);
    }

    private void modifySshManagerMap(final SshManagerDto sshManagerDto){
        final SSHManager ssh= sshManagerRepository.save(sshManagerDto.to());
        this.sshManagerMap.put(ssh.getId(), ssh.toSshManager());
    }
}
