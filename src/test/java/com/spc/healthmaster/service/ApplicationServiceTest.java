package com.spc.healthmaster.service;

import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import com.spc.healthmaster.entity.Application;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.ApplicationRepository;
import com.spc.healthmaster.repository.ServerManagerRepository;
import com.spc.healthmaster.services.application.ApplicationService;
import com.spc.healthmaster.services.application.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.spc.healthmaster.factories.ApiErrorFactory.*;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {
    private final Long serverManagerId = 1l;
    private final Long applicationId = 1l;

    @Mock
    private ServerManagerRepository serverManagerRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    private ApplicationService applicationService;

    @BeforeEach
    public void setup() {
        applicationService =
                new ApplicationServiceImpl(applicationRepository, serverManagerRepository);
    }

    @Test
    public void whenCallFindByServerManagerIdThenReturnList() {
        final List<Application> sshManagers = Arrays.asList(mock(Application.class), mock(Application.class));
        when(applicationRepository.findByServerManagerId(serverManagerId)).thenReturn(sshManagers);
        final List<ApplicationRequestResponseDto> result = applicationService.findAllApplicationByServerManagerId(serverManagerId);
        assertEquals(2, result.size());
    }

    @Test
    public void whenCallFindByServerManagerIdThenReturnEmpty(){
        when(applicationRepository.findByServerManagerId(1l)).thenReturn(Collections.emptyList());
        final List<ApplicationRequestResponseDto> result =  applicationService.findAllApplicationByServerManagerId(serverManagerId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenApplicationIdWhenCallDeleteByIdRemoveId() throws ApiException {
        applicationService.delete(applicationId);
        verify(applicationRepository).deleteById(applicationId);
    }

    @Test
    public void WhenCallDeleteByIdAndNotFoundDataThenApiException () {
        doThrow(EmptyResultDataAccessException.class).when(applicationRepository).deleteById(applicationId);
        final ApiException exception = assertThrows(ApiException.class, () -> applicationService.delete(applicationId));
        assertEquals(exception.getError(), jpaException(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test()
    public void whenTrySaveAndExistInDataBaseThenThrowApiException()  {
        when(applicationRepository.findByApplicationNameAndServerManagerId(anyString(), anyLong()))
                .thenReturn(Optional.of(mock(Application.class)));
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");
        final ApiException exception =  assertThrows(ApiException.class,()-> applicationService.save(request));
        assertEquals(exception.getError(), alreadyExistApplication(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }

    @Test()
    public void whenTrySaveAndNotFoundServerThenThrowApiException()  {
        when(applicationRepository.findByApplicationNameAndServerManagerId(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");
        when(serverManagerRepository.findById(any())).thenReturn(Optional.empty());
        final ApiException exception =  assertThrows(ApiException.class,()-> applicationService.save(request));
        assertEquals(exception.getError(), notFoundServerManager(1l).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test()
    public void whenTrySaveThenApiException()  {
        when(applicationRepository.findByApplicationNameAndServerManagerId(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(mock(ServerManager.class)));

        doThrow(PersistenceException.class).when(applicationRepository).save(any());
        final ApiException exception =  assertThrows(ApiException.class,()-> applicationService.save(request));
        assertEquals(exception.getError(), jpaException(null).getError());
    }

    @Test()
    public void whenTrySaveThenOk() throws ApiException {
        when(applicationRepository.findByApplicationNameAndServerManagerId(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");

        final ServerManager mockServerManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(any())).thenReturn(Optional.of(mockServerManager));
        when(request.toApplication(mockServerManager)).thenReturn(mock(Application.class));
        applicationService.save(request);
    }

    @Test
    public void givenApplicationNotExistWhenEditThenApiException()  {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());
        final ApiException exception = assertThrows(ApiException.class,()-> applicationService.edit(request));
        assertEquals(exception.getError(), notFoundApplication(applicationId).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test
    public void givenApplicationAndNotFoundServerManagerWhenEditThenApiException()  {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(mock(Application.class)));
        when(serverManagerRepository.findById(anyLong())).thenReturn(Optional.empty());
        final ApiException exception = assertThrows(ApiException.class,()-> applicationService.edit(request));
        assertEquals(exception.getError(), notFoundServerManager(serverManagerId).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test
    public void givenApplicationWhenEditThenDoNothing() throws ApiException {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        final Application application = mock(Application.class);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));

        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        when(request.toApplication(serverManager)).thenReturn(application);
        applicationService.edit(request);
        verify(applicationRepository, never())
                .findByApplicationNameAndServerManagerId( anyString(), anyLong());
    }

    @Test
    public void givenServerManagerAndExistOtherServerManagerWhenEditThenApiException() {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");

        final Application application = mock(Application.class);
        when(applicationRepository.findById(serverManagerId)).thenReturn(Optional.of(application));

        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        when(request.toApplication(serverManager)).thenReturn(mock(Application.class));
        when(applicationRepository.findByApplicationNameAndServerManagerId( anyString(), anyLong()))
                .thenReturn(Optional.of(application));
        final ApiException exception = assertThrows(ApiException.class,()-> applicationService.edit(request));
        assertEquals(exception.getError(), alreadyExistApplication(exception.getError()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }



    @Test
    public void WhenEditThenBadDatabaseApiException() {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");

        final Application application = mock(Application.class);
        when(applicationRepository.findById(serverManagerId)).thenReturn(Optional.of(application));

        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        when(request.toApplication(serverManager)).thenReturn(mock(Application.class));
        when(applicationRepository.findByApplicationNameAndServerManagerId( anyString(), anyLong()))
                .thenReturn(Optional.empty());
        doThrow(PersistenceException.class).when(applicationRepository).save(any());
        final ApiException exception = assertThrows(ApiException.class,()-> applicationService.edit(request));
        assertEquals(exception.getError(), jpaException(exception.getError()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void WhenEditThenOk() throws ApiException {
        final ApplicationRequestResponseDto request = mock(ApplicationRequestResponseDto.class);
        when(request.getId()).thenReturn(applicationId);
        when(request.getServerManagerId()).thenReturn(serverManagerId);
        when(request.getApplicationName()).thenReturn("");

        final Application application = mock(Application.class);
        when(applicationRepository.findById(serverManagerId)).thenReturn(Optional.of(application));

        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        when(request.toApplication(serverManager)).thenReturn(mock(Application.class));
        when(applicationRepository.findByApplicationNameAndServerManagerId( anyString(), anyLong()))
                .thenReturn(Optional.empty());
        applicationService.edit(request);
    }
}
