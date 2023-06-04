package com.spc.healthmaster.entity;


import com.spc.healthmaster.dtos.request.RequestResponseSshManagerDto;
import com.spc.healthmaster.dtos.SshManagerDto;
import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Table(name = "ssh_manager")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public final class SSHManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String serverName;
    private String host;
    private String userName;
    private String password;

    public SshManagerDto toSshManagerDto() {
        return new SshManagerDto(this.id,this.serverName, this.host, this.userName, this.password);
    }

    public RequestResponseSshManagerDto toRequestServerDto() {
        return new RequestResponseSshManagerDto(serverName, id, host, userName, password);
    }
}
