package com.spc.healthmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "server_manager_id")
    private final ServerManager serverManager;
}
