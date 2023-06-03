package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.RequestServerDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface SshManagerService {

    List<SshManagerDto> getListSshManager();
    void deleteShhManager(Long id) throws ApiException;
    void save(RequestServerDto sshManagerDto) throws ApiException;
    void edit(RequestServerDto sshManagerDto) throws ApiException;
}
