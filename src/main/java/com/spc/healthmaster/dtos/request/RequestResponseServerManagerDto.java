package com.spc.healthmaster.dtos.request;

import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseServerManagerDto {

    private Long id;
    private TypeStrategy typeStrategy;
    private String serverManagerName;
    private String username;
    private String password;
    private String port;
    private Long ssManagerId;

    public ServerManager toServerManager(final SSHManager sshManager) {
        return new ServerManager(id,typeStrategy, serverManagerName, username, password, port, sshManager);
    }
}
