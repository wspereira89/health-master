package com.spc.healthmaster.configuration;

import com.spc.healthmaster.ssh.dto.SshManagerDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Config {

    @Bean
    public Map<String, SshManagerDto> sshManager() {
        return  new ConcurrentHashMap<>();
    }
}
