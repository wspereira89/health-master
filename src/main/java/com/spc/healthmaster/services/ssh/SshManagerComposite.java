package com.spc.healthmaster.services.ssh;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.exception.ApiException;

public interface SshManagerComposite {

    SshManagerDto getSshManagerMapById(final Long serverId) throws ApiException;
}
