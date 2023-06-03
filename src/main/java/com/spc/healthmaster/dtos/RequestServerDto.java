package com.spc.healthmaster.dtos;

import com.spc.healthmaster.entity.SSHManager;
import lombok.Data;


@Data
public class RequestServerDto {
    private final String serverName;
    private final Long id;
    private final String host;
    private final String user;
    private final String password;

    public SSHManager toSshManager() {
        return SSHManager.builder()
                .id(id)
                .serverName(serverName)
                .userName(user)
                .password(password)
                .host(host)
                .build();
    }

    public SshManagerDto toSshManagerDto(){
        return new SshManagerDto(id, serverName, host, user, password);
    }
}
