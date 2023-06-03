package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;

@Service
public class SshManagerServiceImpl implements SshManagerService {
    private final SSHManagerRepository sshManagerRepository;
    private final Map<Long, SshManagerDto> sshManagerMap;

    public SshManagerServiceImpl(
            final SSHManagerRepository sshManagerRepository,
            final Map<Long, SshManagerDto> sshManagerMap
    ) {
        this.sshManagerRepository = sshManagerRepository;
        this.sshManagerMap = sshManagerMap;
    }

    @Override
    public List<SshManagerDto> getListSshManager() {
        return sshManagerRepository.findAll()
                .stream()
                .map(SSHManager::toSshManagerDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteShhManager(final Long id) throws ApiException {
        //Todo pedir al señor orlando que las bd tengan constraint no se elimine si es usada en otro lado el id
        try {
            sshManagerRepository.deleteById(id);
            this.sshManagerMap.remove(id);
        } catch (final DataAccessException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }

    @Override
    public void save(final RequestServerDto requestServerDto) throws ApiException {
        final boolean existSsh = sshManagerRepository
                .findByServerNameAndHostAndUserName(requestServerDto.getServerName(), requestServerDto.getHost(), requestServerDto.getUser())
                .isPresent();
        if(existSsh) {
            throw alreadyExistServer(requestServerDto.getServerName()).toException();
        }
        validConnectionSsh(requestServerDto.toSshManagerDto());
        modifySshManagerMap(requestServerDto);
    }

    @Override
    public void edit(RequestServerDto requestServerDto) throws ApiException {
        final SSHManager sshManager = sshManagerRepository.findById(requestServerDto.getId())
                        .orElseThrow(()->notFoundServerManager(requestServerDto.getId()).toException());
        final SSHManager other = requestServerDto.toSshManager();
        if(other.equals(sshManager)) {
            return;
        }

        final Optional<SSHManager> findSshManager = sshManagerRepository
                .findByServerNameAndHostAndUserName(requestServerDto.getServerName(), requestServerDto.getHost(), requestServerDto.getUser());
        if(findSshManager.isPresent() && !findSshManager.get().getId().equals(requestServerDto.getId())) {
            throw alreadyExistServer(requestServerDto.getServerName()).toException();
        }

        validConnectionSsh(requestServerDto.toSshManagerDto());
        modifySshManagerMap(requestServerDto);
    }

    private void modifySshManagerMap(final RequestServerDto sshManagerDto) throws ApiException {
        try {
            final SSHManager ssh = sshManagerRepository.save(sshManagerDto.toSshManager());
            this.sshManagerMap.put(ssh.getId(), ssh.toSshManagerDto());
        } catch (final DataAccessException | PersistenceException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }

    private void validConnectionSsh(final SshManagerDto sshManagerDto) throws ApiException {
            sshManagerDto.connect();
    }
}
