package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.request.RequestResponseSshManagerDto;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface SshManagerService {

    List<RequestResponseSshManagerDto> findAll();
    void delete(Long id) throws ApiException;
    void save(RequestResponseSshManagerDto sshManagerDto) throws ApiException;
    void edit(RequestResponseSshManagerDto sshManagerDto) throws ApiException;
}
