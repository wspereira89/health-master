package com.spc.healthmaster.ssh;

import com.spc.healthmaster.ssh.dto.SshManagerDto;

public interface SshManagerComposite {
    void addServer();

    SshManagerDto getSshManagerMapById(final String serverId) throws Exception;
}
