package com.spc.healthmaster.entity;


import com.spc.healthmaster.dtos.SshManagerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "ssh_manager")
@AllArgsConstructor
public final class SSHManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final String serverName;
    private final String host;
    private final String userName;
    private final String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ssh_manager_id")
    private List<ServerManager> serverManagers = new ArrayList<>();

    // Métodos para administrar la lista de serverManagers
    public void addServerManager(ServerManager serverManager) {
        serverManagers.add(serverManager);
    }

    public void removeServerManager(ServerManager serverManager) {
        serverManagers.remove(serverManager);
    }

    public SshManagerDto toSshManager() {
        return new SshManagerDto(this.id,this.serverName, this.host, this.userName, this.password);
    }
}
