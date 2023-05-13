package com.spc.healthmaster;

import com.spc.healthmaster.ssh.SshManagerCompositeImpl;
import com.spc.healthmaster.ssh.dto.SshManagerDto;
import com.spc.healthmaster.ssh.repository.SshManagerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@SpringBootApplication
public class HealthMasterApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(HealthMasterApplication.class, args);
        SshManagerRepository  repository = context.getBean(SshManagerRepository.class) ;
        Map<String, SshManagerDto> managerDtoMap = context.getBean("sshManager", Map.class);
        SshManagerCompositeImpl componet = new SshManagerCompositeImpl(repository,managerDtoMap);
        componet.addServer();
       System.out.println( componet.getSshManagerMapById("1"));
    }

}
