package com.spc.healthmaster;

import com.jcraft.jsch.JSchException;
import com.spc.healthmaster.ssh.SshManagerComposite;
import com.spc.healthmaster.ssh.repository.SshManagerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class HealthMasterApplication {

    public static void main(String[] args) throws JSchException, IOException, IllegalAccessException {
        ApplicationContext context = SpringApplication.run(HealthMasterApplication.class, args);
        SshManagerRepository  repository = context.getBean(SshManagerRepository.class) ;
        SshManagerComposite componet = new SshManagerComposite(repository);
        componet.addServer();
        componet.getSshManagerMap().get("1").connect();
        System.out.println(componet.getSshManagerMap().get("1").executeCommand("ls -l"));
    }

}
