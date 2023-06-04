package com.spc.healthmaster.services.server;

import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface ServerManagerService {

    List<RequestResponseServerManagerDto> findBySshManagerId(Long sshManagerId);
    void delete(Long id) throws ApiException;
    void save(RequestResponseServerManagerDto request) throws ApiException;
    void edit(RequestResponseServerManagerDto request) throws ApiException;
}
