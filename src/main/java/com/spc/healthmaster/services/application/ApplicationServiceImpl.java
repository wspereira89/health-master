package com.spc.healthmaster.services.application;

import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.repository.ServerManagerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ServerManagerRepository serverManagerRepository;

    public ApplicationServiceImpl(final ApplicationRepository applicationRepository, final ServerManagerRepository serverManagerRepository) {
        this.applicationRepository = applicationRepository;
        this.serverManagerRepository = serverManagerRepository;
    }

    @Override
    public List<ApplicationRequestResponseDto> findAllApplicationByServerManagerId(final Long serverManagerId) {
        return applicationRepository.findByServerManagerId(serverManagerId).stream()
                .map(Application::toRequest)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(final Long id) throws ApiException {
        //Todo pedir al señor orlando que las bd tengan constraint no se elimine si es usada en otro lado el id
        try {
            applicationRepository.deleteById(id);
        } catch (final DataAccessException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }

    @Override
    public void save(final ApplicationRequestResponseDto request) throws ApiException {
        final boolean exist = applicationRepository
                .findByApplicationNameAndServerManagerId(request.getApplicationName(), request.getServerManagerId())
                .isPresent();
        if(exist) {
            throw alreadyExistApplication(request.getApplicationName()).toException();
        }

        final ServerManager serverManager = request.getServerManagerId() == 0
                ? null
                : serverManagerRepository.findById(request.getServerManagerId())
                .orElseThrow(()->notFoundServerManager(request.getServerManagerId()).toException());
        modifyServerManager(request, serverManager);
    }

    @Override
    public void edit(final ApplicationRequestResponseDto request) throws ApiException {
       final Application application= applicationRepository
               .findById(request.getId())
               .orElseThrow(()->notFoundApplication(request.getId()).toException());
        final ServerManager serverManager =  request.getServerManagerId() ==0
                ? null
                : serverManagerRepository.findById(request.getServerManagerId())
                .orElseThrow(()->notFoundServerManager(request.getServerManagerId()).toException());
        final Application other = request.toApplication(serverManager);

        if(other.equals(application)) {
            return;
        }

        final Optional<Application> findApplication = applicationRepository
                .findByApplicationNameAndServerManagerId(request.getApplicationName(), request.getServerManagerId());

        if(findApplication.isPresent() && !findApplication.get().getId().equals(request.getId())) {
            throw alreadyExistApplication(request.getApplicationName()).toException();
        }
        modifyServerManager(request, serverManager);
    }

    private void modifyServerManager(final ApplicationRequestResponseDto request, final ServerManager serverManager) throws ApiException {
        try {
            applicationRepository.save(request.toApplication(serverManager));
        } catch (final DataAccessException | PersistenceException ex) {
            throw jpaException(ex.getMessage()).toException();
        }
    }
}
