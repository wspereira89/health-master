package com.spc.healthmaster.service;

import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import com.spc.healthmaster.services.ssh.SshManagerService;
import com.spc.healthmaster.services.ssh.SshManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.PersistenceException;
import java.util.*;

import static com.spc.healthmaster.factories.ApiErrorFactory.alreadyExistServer;
import static com.spc.healthmaster.factories.ApiErrorFactory.jpaException;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SshManagerServiceTest {

    private final Long sshManagerId = 1L;
    private final String PASSWORD ="EMPTY";
    private final String OTHER_PASSWORD = "OTHER";

    @Mock
    private SSHManagerRepository sshManagerRepository;

    @Mock
    private Map<Long, SshManagerDto> sshManagerMap;

    private SshManagerService sshManagerService;

    @BeforeEach
    public void setup() {
        sshManagerService =
                new SshManagerServiceImpl(sshManagerRepository, sshManagerMap);
    }

    @Test
    public void whenCallGetListShhManagerThenReturnList() {
        final List<SSHManager> sshManagers = Arrays.asList(getSshManager(1l, PASSWORD).get(), getSshManager(2l, PASSWORD).get());
        when(sshManagerRepository.findAll()).thenReturn(sshManagers);
        final List<SshManagerDto> result = sshManagerService.getListSshManager();
        assertEquals(2, result.size());
    }

    @Test
    public void whenCallGetListShhManagerThenReturnEmpty(){
        when(sshManagerRepository.findAll()).thenReturn(Collections.emptyList());
        final List<SshManagerDto> result = sshManagerService.getListSshManager();
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenManagerIdWhenCallDeleteByIdRemoveId() throws ApiException {
        sshManagerService.deleteShhManager(sshManagerId);
        verify(sshManagerRepository).deleteById(sshManagerId);
        verify(sshManagerMap).remove(sshManagerId);
    }

    @Test
    public void WhenCallDeleteByIdAndNotFoundDataThenApiException () {

        doThrow(EmptyResultDataAccessException.class).when(sshManagerRepository).deleteById(sshManagerId);
        final ApiException exception = assertThrows(ApiException.class, () -> sshManagerService.deleteShhManager(sshManagerId));
        verify(sshManagerMap, never()).remove(sshManagerId);
        assertEquals(exception.getError(), jpaException(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test()
    public void whenTrySaveAndExistInDataBaseThenThrowApiException()  {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(getSshManager(1l, PASSWORD));
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getServerName()).thenReturn("");
        when(requestServerDto.getHost()).thenReturn("");
        when(requestServerDto.getUser()).thenReturn("");
        final ApiException exception =  assertThrows(ApiException.class,()-> sshManagerService.save(requestServerDto));
        assertEquals(exception.getError(), alreadyExistServer(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }

    @Test
    public void whenTrySaveAndConnectionShhInvalidThenApiException() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getServerName()).thenReturn("");
        when(requestServerDto.getHost()).thenReturn("");
        when(requestServerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doThrow(ApiException.class).when(sshManagerDto).connect();
        assertThrows(ApiException.class,()-> sshManagerService.save(requestServerDto));
    }

    @Test
    public void whenTrySaveThenApiException() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getServerName()).thenReturn("");
        when(requestServerDto.getHost()).thenReturn("");
        when(requestServerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        doThrow(PersistenceException.class).when(sshManagerRepository).save(any());
        assertThrows(ApiException.class,()-> sshManagerService.save(requestServerDto));
    }


    @Test
    public void whenTrySaveSshManagerThenOk() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getServerName()).thenReturn("");
        when(requestServerDto.getHost()).thenReturn("");
        when(requestServerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        when(sshManagerRepository.save(any())).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.save(requestServerDto);
        verify(sshManagerMap, times(1)).put(any(), any());
    }

    @Test
    public void givenInvalidIdWhenEditThenThrowApiException() {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.empty());
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestServerDto));
    }

    @Test
    public void whenRequestEqualsToDataWhenEditThenNoting() throws ApiException {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.edit(requestServerDto);
        verify(sshManagerRepository, never()).findByServerNameAndHostAndUserName(any(), any(),any());
    }

    @Test
    public void givenSshManagerExistWhenEditThenApiException() throws ApiException {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(2l, PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(2l, PASSWORD));
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestServerDto));
    }

    @Test
    public void whenTryEditAndConnectionShhInvalidThenApiException() throws ApiException {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doThrow(ApiException.class).when(sshManagerDto).connect();
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestServerDto));
    }

    @Test
    public void whenTryEditThenApiException() throws ApiException {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        doThrow(PersistenceException.class).when(sshManagerRepository).save(any());
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestServerDto));
    }

    @Test
    public void whenTryEditThenOk() throws ApiException {
        final RequestServerDto requestServerDto = mock(RequestServerDto.class);
        when(requestServerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestServerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestServerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        when(sshManagerRepository.save(any())).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.edit(requestServerDto);
        verify(sshManagerMap, times(1)).put(any(), any());
    }

    private Optional<SSHManager> getSshManager(final Long id, final String password){
        return Optional.of(SSHManager.builder().id(id).password(password).build());
    }
}
