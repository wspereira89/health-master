package com.spc.healthmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableJpaRepositories("com.spc.healthmaster.repository")
@EntityScan("com.spc.healthmaster.entity")
public class HealthMasterApplication {

    public static void main(String[] args) {
       SpringApplication.run(HealthMasterApplication.class, args);
    }

}
