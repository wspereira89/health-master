package com.spc.healthmaster.service;

import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import com.spc.healthmaster.repository.ServerManagerRepository;
import com.spc.healthmaster.services.server.ServerManagerService;
import com.spc.healthmaster.services.server.ServerManagerServiceImpl;
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
public class ServerManagerServiceTest {
    private final Long sshManagerId = 1l;
    private final Long serverManagerId = 1l;

    @Mock
    private SSHManagerRepository sshManagerRepository;
    @Mock
    private ServerManagerRepository serverManagerRepository;
    private ServerManagerService serverManagerService;

    @BeforeEach
    public void setup() {
        serverManagerService =
                new ServerManagerServiceImpl(serverManagerRepository, sshManagerRepository);
    }

    @Test
    public void whenCallFindBySshManagerIdThenReturnList() {
        final List<ServerManager> sshManagers = Arrays.asList(mock(ServerManager.class), mock(ServerManager.class));
        when(serverManagerRepository.findAllBySshManagerId(sshManagerId)).thenReturn(sshManagers);
        final List<RequestResponseServerManagerDto> result = serverManagerService.findServerManagerBySshManagerId(sshManagerId);
        assertEquals(2, result.size());
    }

    @Test
    public void whenCallFindBySshManagerIdThenReturnEmpty(){
        when(serverManagerRepository.findAllBySshManagerId(1l)).thenReturn(Collections.emptyList());
        final List<RequestResponseServerManagerDto> result =  serverManagerService.findServerManagerBySshManagerId(sshManagerId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenServerManagerIdWhenCallDeleteByIdRemoveId() throws ApiException {
        serverManagerService.delete(serverManagerId);
        verify(serverManagerRepository).deleteById(serverManagerId);
    }

    @Test
    public void WhenCallDeleteByIdAndNotFoundDataThenApiException () {
        doThrow(EmptyResultDataAccessException.class).when(serverManagerRepository).deleteById(serverManagerId);
        final ApiException exception = assertThrows(ApiException.class, () -> serverManagerService.delete(serverManagerId));
        assertEquals(exception.getError(), jpaException(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test()
    public void whenTrySaveAndExistInDataBaseThenThrowApiException()  {
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.of(mock(ServerManager.class)));
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        final ApiException exception =  assertThrows(ApiException.class,()-> serverManagerService.save(request));
        assertEquals(exception.getError(), alreadyExistServerManager(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }
    @Test()
    public void whenTrySaveAndNotFoundSshConnectionThenThrowApiException()  {
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.empty());
        final ApiException exception =  assertThrows(ApiException.class,()-> serverManagerService.save(request));
        assertEquals(exception.getError(), notFoundConnectionSsh(sshManagerId).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test
    public void whenTrySaveThenApiException() {
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(mock(SSHManager.class)));
        doThrow(PersistenceException.class).when(serverManagerRepository).save(any());
        final ApiException exception =  assertThrows(ApiException.class,()-> serverManagerService.save(request));
        assertEquals(exception.getError(), jpaException(exception.getError()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void whenTrySaveThenOk() throws ApiException {
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.empty());
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        final SSHManager mockSshManager = mock(SSHManager.class);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(mockSshManager));
        final ServerManager mockServerManager = mock(ServerManager.class);
        when(request.toServerManager(mockSshManager)).thenReturn(mockServerManager);
        serverManagerService.save(request);
    }

    @Test
    public void givenServerManagerNotExistWhenEditThenApiException()  {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.empty());
        final ApiException exception = assertThrows(ApiException.class,()-> serverManagerService.edit(request));
        assertEquals(exception.getError(), notFoundServerManager(serverManagerId).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test
    public void givenServerManagerNotExistSshConnectionWhenEditThenApiException()  {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(mock(ServerManager.class)));
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.empty());
        final ApiException exception = assertThrows(ApiException.class,()-> serverManagerService.edit(request));
        assertEquals(exception.getError(), notFoundConnectionSsh(sshManagerId).getError());
        assertEquals(exception.getStatus() , SC_NOT_FOUND);
    }

    @Test
    public void givenServerManagerWhenEditThenDoNothing() throws ApiException {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        final SSHManager sshManager = mock(SSHManager.class);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(sshManager));
        when(request.toServerManager(sshManager)).thenReturn(serverManager);
        serverManagerService.edit(request);
        verify(serverManagerRepository , never())
                .findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong());
    }

    @Test
    public void givenServerManagerAndExistOtherServerManagerWhenEditThenApiException() {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        final SSHManager sshManager = mock(SSHManager.class);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(sshManager));
        when(request.toServerManager(sshManager)).thenReturn(mock(ServerManager.class));
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.of(serverManager));
        final ApiException exception = assertThrows(ApiException.class,()-> serverManagerService.edit(request));
        assertEquals(exception.getError(), alreadyExistServerManager(exception.getError()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }

    @Test
    public void WhenEditThenBadDatabaseApiException() {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        final SSHManager sshManager = mock(SSHManager.class);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(sshManager));
        when(request.toServerManager(sshManager)).thenReturn(mock(ServerManager.class));
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.empty());
        doThrow(PersistenceException.class).when(serverManagerRepository).save(any());
        final ApiException exception = assertThrows(ApiException.class,()-> serverManagerService.edit(request));
        assertEquals(exception.getError(), jpaException(exception.getError()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void WhenEditThenOk() throws ApiException {
        final RequestResponseServerManagerDto request = mock(RequestResponseServerManagerDto.class);
        when(request.getId()).thenReturn(serverManagerId);
        when(request.getSsManagerId()).thenReturn(sshManagerId);
        when(request.getTypeStrategy()).thenReturn(TypeStrategy.GLASSFISH_APP);
        when(request.getUsername()).thenReturn("");
        final ServerManager serverManager = mock(ServerManager.class);
        when(serverManagerRepository.findById(serverManagerId)).thenReturn(Optional.of(serverManager));
        final SSHManager sshManager = mock(SSHManager.class);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.of(sshManager));
        when(request.toServerManager(sshManager)).thenReturn(mock(ServerManager.class));
        when(serverManagerRepository.findAllByTypeStrategyAndUsernameAndSshManagerId(any(TypeStrategy.class), anyString(), anyLong()))
                .thenReturn(Optional.empty());
        serverManagerService.edit(request);
    }
}
