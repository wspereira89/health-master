package com.spc.healthmaster.services.application;

import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import com.spc.healthmaster.exception.ApiException;

import java.util.List;

public interface ApplicationService {

    List<ApplicationRequestResponseDto>  findAllApplicationByServerManagerId(Long serverManagerId);


    void delete(Long id) throws ApiException;
    void save(ApplicationRequestResponseDto request) throws ApiException;
    void edit(ApplicationRequestResponseDto request) throws ApiException;
}
