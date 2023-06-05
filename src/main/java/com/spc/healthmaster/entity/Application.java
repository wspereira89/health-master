package com.spc.healthmaster.entity;

import com.spc.healthmaster.dtos.request.ApplicationRequestResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Data
@Entity
@Table(name = "application")
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final String applicationName;
    private final String pathFile;
    private final Integer jmxPort;
    private final String memory;

    @ManyToOne
    @JoinColumn(name = "server_manager_id")
    private final ServerManager serverManager;

    public ApplicationRequestResponseDto toRequest() {

        return new ApplicationRequestResponseDto(id, applicationName, pathFile, jmxPort, memory, serverManager.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(applicationName, that.applicationName)
                .append(pathFile, that.pathFile)
                .append(jmxPort, that.jmxPort)
                .append(memory, that.memory)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(applicationName)
                .append(pathFile)
                .append(jmxPort)
                .append(memory)
                .toHashCode();
    }
}
