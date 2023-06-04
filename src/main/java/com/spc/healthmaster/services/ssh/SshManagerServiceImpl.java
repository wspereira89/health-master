package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.request.RequestResponseSshManagerDto;
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
    public List<RequestResponseSshManagerDto> findAll() {
        return sshManagerRepository.findAll()
                .stream()
                .map(SSHManager::toRequestServerDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(final Long id) throws ApiException {
        //Todo pedir al señor orlando que las bd tengan constraint no se elimine si es usada en otro lado el id
        try {
            sshManagerRepository.deleteById(id);
            this.sshManagerMap.remove(id);
        } catch (final DataAccessException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }

    @Override
    public void save(final RequestResponseSshManagerDto requestResponseSshManagerDto) throws ApiException {
        final boolean existSsh = sshManagerRepository
                .findByServerNameAndHostAndUserName(requestResponseSshManagerDto.getServerName(), requestResponseSshManagerDto.getHost(), requestResponseSshManagerDto.getUser())
                .isPresent();
        if(existSsh) {
            throw alreadyExistSshManager(requestResponseSshManagerDto.getServerName()).toException();
        }
       // validConnectionSsh(requestResponseSshManagerDto.toSshManagerDto());
        modifySshManagerMap(requestResponseSshManagerDto);
    }

    @Override
    public void edit(RequestResponseSshManagerDto requestResponseSshManagerDto) throws ApiException {
        final SSHManager sshManager = sshManagerRepository.findById(requestResponseSshManagerDto.getId())
                        .orElseThrow(()->notFoundConnectionSsh(requestResponseSshManagerDto.getId()).toException());
        final SSHManager other = requestResponseSshManagerDto.toSshManager();
        if(other.equals(sshManager)) {
            return;
        }

        final Optional<SSHManager> findSshManager = sshManagerRepository
                .findByServerNameAndHostAndUserName(requestResponseSshManagerDto.getServerName(), requestResponseSshManagerDto.getHost(), requestResponseSshManagerDto.getUser());
        if(findSshManager.isPresent() && !findSshManager.get().getId().equals(requestResponseSshManagerDto.getId())) {
            throw alreadyExistSshManager(requestResponseSshManagerDto.getServerName()).toException();
        }

       // validConnectionSsh(requestResponseSshManagerDto.toSshManagerDto());
        modifySshManagerMap(requestResponseSshManagerDto);
    }

    private void modifySshManagerMap(final RequestResponseSshManagerDto sshManagerDto) throws ApiException {
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
