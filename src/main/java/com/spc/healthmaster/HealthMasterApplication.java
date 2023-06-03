package com.spc.healthmaster;

import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.dtos.WrapperExecute;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.strategy.CommandStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableJpaRepositories("com.spc.healthmaster.repository")
@EntityScan("com.spc.healthmaster.entity")
public class HealthMasterApplication {

    public static void main(String[] args) throws ApiException {
        ApplicationContext context = SpringApplication.run(HealthMasterApplication.class, args);
        
        CommandStrategy spring = context.getBean("glassfishServerCommandStrategy", CommandStrategy.class);
        SshManagerDto ssh= new SshManagerDto(1L, "a","10.18.100.30", "usrappsvr","Lm3e$Pkl34");
        WrapperExecute wrapper = WrapperExecute.builder()
                .sshManagerDto(ssh)
                .build();
        spring.start(wrapper);
    }

}
