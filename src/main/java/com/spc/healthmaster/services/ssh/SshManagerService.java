package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface SshManagerService {

    List<SshManagerDto> getListSshManager();
    void deleteShhManager(Long id);
    void saved(SshManagerDto sshManagerDto) throws ApiException;
    void edit(SshManagerDto sshManagerDto) throws ApiException;
}
