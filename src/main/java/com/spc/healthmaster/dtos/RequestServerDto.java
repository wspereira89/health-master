package com.spc.healthmaster.dtos;

import com.spc.healthmaster.entity.SSHManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestServerDto {
    @NotNull(message = "Invalid  server Name")
    private String serverName;
    private Long id;

    @NotNull(message = "Invalid Host")
    private String host;

    @NotNull(message = "Invalid  user")
    private String user;

    @NotNull(message = "Invalid passwrod")
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
