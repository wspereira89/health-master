package com.spc.healthmaster.dtos;

import com.spc.healthmaster.entity.SSHManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestServerDto {
    private String serverName;
    private Long id;
    private String host;
    private String user;
    private String password;

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
