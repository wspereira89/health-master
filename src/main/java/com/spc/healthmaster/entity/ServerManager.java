package com.spc.healthmaster.entity;

import com.spc.healthmaster.dtos.request.RequestResponseServerManagerDto;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Data
@Entity
@Table(name = "server_manager")
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeStrategy typeStrategy;
    private String serverManagerName;
    private String username;
    private String password;
    private String port;


    @ManyToOne
    @JoinColumn(name = "ssh_manager_id")
    private SSHManager sshManager;

    public RequestResponseServerManagerDto toRequest() {
        return new RequestResponseServerManagerDto(id, typeStrategy, serverManagerName, username, password, port, sshManager.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServerManager that = (ServerManager) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(typeStrategy, that.typeStrategy)
                .append(serverManagerName, that.serverManagerName)
                .append(username, that.username)
                .append(password, that.password)
                .append(port, that.port)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(typeStrategy)
                .append(serverManagerName)
                .append(username)
                .append(password)
                .append(port)
                .toHashCode();
    }
}
