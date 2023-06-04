package com.spc.healthmaster.service;

import com.spc.healthmaster.dtos.request.RequestResponseSshManagerDto;
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

import static com.spc.healthmaster.factories.ApiErrorFactory.alreadyExistSshManager;
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
        final List<RequestResponseSshManagerDto> result = sshManagerService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void whenCallGetListShhManagerThenReturnEmpty(){
        when(sshManagerRepository.findAll()).thenReturn(Collections.emptyList());
        final List<RequestResponseSshManagerDto> result = sshManagerService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void givenManagerIdWhenCallDeleteByIdRemoveId() throws ApiException {
        sshManagerService.delete(sshManagerId);
        verify(sshManagerRepository).deleteById(sshManagerId);
        verify(sshManagerMap).remove(sshManagerId);
    }

    @Test
    public void WhenCallDeleteByIdAndNotFoundDataThenApiException () {

        doThrow(EmptyResultDataAccessException.class).when(sshManagerRepository).deleteById(sshManagerId);
        final ApiException exception = assertThrows(ApiException.class, () -> sshManagerService.delete(sshManagerId));
        verify(sshManagerMap, never()).remove(sshManagerId);
        assertEquals(exception.getError(), jpaException(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_INTERNAL_SERVER_ERROR);
    }

    @Test()
    public void whenTrySaveAndExistInDataBaseThenThrowApiException()  {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(getSshManager(1l, PASSWORD));
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getServerName()).thenReturn("");
        when(requestResponseSshManagerDto.getHost()).thenReturn("");
        when(requestResponseSshManagerDto.getUser()).thenReturn("");
        final ApiException exception =  assertThrows(ApiException.class,()-> sshManagerService.save(requestResponseSshManagerDto));
        assertEquals(exception.getError(), alreadyExistSshManager(exception.getMessage()).getError());
        assertEquals(exception.getStatus() , SC_BAD_REQUEST);
    }

    @Test
    public void whenTrySaveAndConnectionShhInvalidThenApiException() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getServerName()).thenReturn("");
        when(requestResponseSshManagerDto.getHost()).thenReturn("");
        when(requestResponseSshManagerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doThrow(ApiException.class).when(sshManagerDto).connect();
        assertThrows(ApiException.class,()-> sshManagerService.save(requestResponseSshManagerDto));
    }

    @Test
    public void whenTrySaveThenApiException() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getServerName()).thenReturn("");
        when(requestResponseSshManagerDto.getHost()).thenReturn("");
        when(requestResponseSshManagerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        doThrow(PersistenceException.class).when(sshManagerRepository).save(any());
        assertThrows(ApiException.class,()-> sshManagerService.save(requestResponseSshManagerDto));
    }


    @Test
    public void whenTrySaveSshManagerThenOk() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getServerName()).thenReturn("");
        when(requestResponseSshManagerDto.getHost()).thenReturn("");
        when(requestResponseSshManagerDto.getUser()).thenReturn("");
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        when(sshManagerRepository.save(any())).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.save(requestResponseSshManagerDto);
        verify(sshManagerMap, times(1)).put(any(), any());
    }

    @Test
    public void givenInvalidIdWhenEditThenThrowApiException() {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(Optional.empty());
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestResponseSshManagerDto));
    }

    @Test
    public void whenRequestEqualsToDataWhenEditThenNoting() throws ApiException {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.edit(requestResponseSshManagerDto);
        verify(sshManagerRepository, never()).findByServerNameAndHostAndUserName(any(), any(),any());
    }

    @Test
    public void givenSshManagerExistWhenEditThenApiException() throws ApiException {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(2l, PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(2l, PASSWORD));
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestResponseSshManagerDto));
    }

    @Test
    public void whenTryEditAndConnectionShhInvalidThenApiException() throws ApiException {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doThrow(ApiException.class).when(sshManagerDto).connect();
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestResponseSshManagerDto));
    }

    @Test
    public void whenTryEditThenApiException() throws ApiException {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        doThrow(PersistenceException.class).when(sshManagerRepository).save(any());
        assertThrows(ApiException.class,()-> sshManagerService.edit(requestResponseSshManagerDto));
    }

    @Test
    public void whenTryEditThenOk() throws ApiException {
        final RequestResponseSshManagerDto requestResponseSshManagerDto = mock(RequestResponseSshManagerDto.class);
        when(requestResponseSshManagerDto.getId()).thenReturn(sshManagerId);
        when(sshManagerRepository.findById(sshManagerId)).thenReturn(getSshManager(sshManagerId, PASSWORD));
        when(requestResponseSshManagerDto.toSshManager()).thenReturn(getSshManager(sshManagerId, OTHER_PASSWORD).get());
        when(sshManagerRepository.findByServerNameAndHostAndUserName(any(), any(), any())).thenReturn(getSshManager(sshManagerId, PASSWORD));
        final SshManagerDto sshManagerDto = mock(SshManagerDto.class);
        when(requestResponseSshManagerDto.toSshManagerDto()).thenReturn(sshManagerDto);
        doNothing().when(sshManagerDto).connect();
        when(sshManagerRepository.save(any())).thenReturn(getSshManager(sshManagerId, PASSWORD).get());
        sshManagerService.edit(requestResponseSshManagerDto);
        verify(sshManagerMap, times(1)).put(any(), any());
    }

    private Optional<SSHManager> getSshManager(final Long id, final String password){
        return Optional.of(SSHManager.builder().id(id).password(password).build());
    }
}
