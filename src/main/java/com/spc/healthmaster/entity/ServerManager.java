package com.spc.healthmaster.entity;

import com.spc.healthmaster.enums.TypeStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "server_manager")
@AllArgsConstructor
public class ServerManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;

    @Enumerated(EnumType.STRING)
    private final TypeStrategy typeStrategy;

    private final String serverManagerName;

    @ManyToOne
    @JoinColumn(name = "ssh_manager_id")
    private final SSHManager sshManager;

    @OneToMany(mappedBy = "serverManager")
    private final List<Application> applications;

    private final String username;
    private final String passwordPath;
    private final String port;
}
