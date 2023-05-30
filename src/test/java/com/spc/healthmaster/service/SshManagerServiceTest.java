package com.spc.healthmaster.service;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.repository.SSHManagerRepository;
import com.spc.healthmaster.services.ssh.SshManagerService;
import com.spc.healthmaster.services.ssh.SshManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class SshManagerServiceTest {

    @Mock
    private SSHManagerRepository sshManagerRepository;

    @Mock
    private Map<Long, SshManagerDto> sshManagerMap;

    private SshManagerService sshManagerService;

    @BeforeEach
    public void setup() {
        openMocks(this);
        sshManagerService =
                new SshManagerServiceImpl(sshManagerRepository, sshManagerMap);
    }

    @Test
    public void whenCallGetListShhManagerThenReturnList() {
        final List<SSHManager> sshManagers = Arrays.asList(getSshManager(1l).get(), getSshManager(2l).get());
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
    public void givenManagerIdWhenCallDeleteByIdRemoveId() {
        final Long sshManagerId = 1L;
        sshManagerService.deleteShhManager(sshManagerId);
        verify(sshManagerRepository).deleteById(sshManagerId);
        verify(sshManagerMap).remove(sshManagerId);
    }

    @Test
    public void whenTrySaveSshManagerThenOk() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        final SSHManager sshManager = SSHManager.builder()
                .id(1L).build();
        when(sshManagerRepository.save(any())).thenReturn(sshManager);
        sshManagerService.saved(getSshManagerDto(1l));
        verify(sshManagerRepository).save(any());
        verify(sshManagerMap).put(anyLong(), any());
    }

    @Test()
    public void whenTrySaveSshManagerThenReturnApiException()  {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(getSshManager(1l));

       assertThrows(ApiException.class,()-> sshManagerService.saved(getSshManagerDto(1l)));
    }

    @Test
    public void testEdit_WhenSshManagerExists() throws ApiException {
        when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(getSshManager(1l));
        when(sshManagerRepository.save(any())).thenReturn(getSshManager(1l).get());
        // Act
        sshManagerService.edit(getSshManagerDto(1l));

        // Assert
        verify(sshManagerRepository).save(any());
        verify(sshManagerMap).put(anyLong(), any());
    }

    @Test()
    public void testEdit_WhenSshManagerDoesNotExist() {
        // Arrange
         when(sshManagerRepository.findByServerNameAndHostAndUserName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ApiException.class,()-> sshManagerService.edit(getSshManagerDto(1l)));
    }

    private SshManagerDto getSshManagerDto(final Long id){
        return new SshManagerDto(id, "Host1", "User1","","");
    }

    private Optional<SSHManager> getSshManager(Long id){
        return Optional.of(SSHManager.builder().id(id).build());
    }
}
