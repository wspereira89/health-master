package com.spc.healthmaster.services.server;

import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import com.spc.healthmaster.repository.ServerManagerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;

@Service
public class ServerManagerServiceImpl implements ServerManagerService {

    private final ServerManagerRepository serverManagerRepository;
    private final SSHManagerRepository sshManagerRepository;

    public ServerManagerServiceImpl(final ServerManagerRepository serverManagerRepository, final SSHManagerRepository sshManagerRepository) {
        this.serverManagerRepository = serverManagerRepository;
        this.sshManagerRepository = sshManagerRepository;
    }

    @Override
    public List<RequestResponseServerManagerDto> findBySshManagerId(final Long sshManagerId) {

        return this.serverManagerRepository.findAllBySshManagerId(sshManagerId)
                .stream()
                .map(ServerManager::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(final Long id) throws ApiException {
        //Todo pedir al señor orlando que las bd tengan constraint no se elimine si es usada en otro lado el id
        try {
            serverManagerRepository.deleteById(id);
        } catch (final DataAccessException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }

    @Override
    public void save(final RequestResponseServerManagerDto request) throws ApiException {

        final boolean existSsh = serverManagerRepository
                .findAllByTypeStrategyAndUsernameAndSshManagerId(
                request.getTypeStrategy(), request.getUsername(), request.getSsManagerId())
                .isPresent();
        if(existSsh) {
            throw alreadyExistServerManager(request.getServerManagerName()).toException();
        }

        final SSHManager sshManager = sshManagerRepository.findById(request.getSsManagerId())
                . orElseThrow(()->notFoundConnectionSsh(request.getSsManagerId()).toException());
        modifyServerManager(request, sshManager);
    }

    @Override
    public void edit(final RequestResponseServerManagerDto request) throws ApiException {
        final ServerManager serverManager = serverManagerRepository.findById(request.getId())
                .orElseThrow(()->notFoundServerManager(request.getId()).toException());
        final SSHManager sshManager = sshManagerRepository.findById(request.getSsManagerId())
                . orElseThrow(()->notFoundConnectionSsh(request.getSsManagerId()).toException());
        final ServerManager other = request.toServerManager(sshManager);
        if(other.equals(serverManager)) {
           return;
        }

        final Optional<ServerManager> findServerManager = serverManagerRepository
                .findAllByTypeStrategyAndUsernameAndSshManagerId(request.getTypeStrategy(), request.getUsername(), request.getSsManagerId());
        if(findServerManager.isPresent() && !findServerManager.get().getId().equals(request.getId())) {
            throw alreadyExistServerManager(request.getUsername()).toException();
        }

        modifyServerManager(request, sshManager);
    }

    private void modifyServerManager(final RequestResponseServerManagerDto serverManager, final SSHManager sshManager) throws ApiException {
        try {
             serverManagerRepository.save(serverManager.toServerManager(sshManager));
        } catch (final DataAccessException | PersistenceException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }
}
