package com.spc.healthmaster.dtos.request;

import com.spc.healthmaster.constrain.ValidTypeStrategy;
import com.spc.healthmaster.entity.SSHManager;
import com.spc.healthmaster.entity.ServerManager;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseServerManagerDto {

    private Long id;
    @ValidTypeStrategy
    private TypeStrategy typeStrategy;

    @NotNull(message = "Invalid serverManagerName")
    private String serverManagerName;

    @NotNull(message = "Invalid username")
    private String username;

    @NotNull(message = "Invalid password")
    private String password;
    @NotNull(message = "Invalid port")
    private String port;

    @NotNull(message = "Invalid ssManagerId")
    private Long ssManagerId;

    public ServerManager toServerManager(final SSHManager sshManager) {
        return new ServerManager(id,typeStrategy, serverManagerName, username, password, port, sshManager);
    }
}
