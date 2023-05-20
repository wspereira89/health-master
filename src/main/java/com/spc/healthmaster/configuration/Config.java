package com.spc.healthmaster.configuration;

import com.google.common.collect.ImmutableMap;
import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.enums.Actions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.spc.healthmaster.command.StartCommandAction.START_COMMAND_ACTION;
import static com.spc.healthmaster.command.StatusCommandAction.STATUS_COMMAND_ACTION;
import static com.spc.healthmaster.command.StopCommandAction.STOP_COMMAND_ACTION;

@Configuration
@EnableAsync
public class Config {

    @Bean
    public Map<String, SshManagerDto> sshManager() {
        return  new ConcurrentHashMap<>();
    }

    @Bean
    public  Map<Actions, CommandAction> actionMap(
            @Qualifier(START_COMMAND_ACTION) CommandAction startAction,
            @Qualifier(STATUS_COMMAND_ACTION) CommandAction statusAction,
            @Qualifier(STOP_COMMAND_ACTION) CommandAction stopAction
    ) {

        return ImmutableMap.of(
            Actions.START, startAction,
            Actions.STATUS, statusAction,
            Actions.STOP, stopAction
        );
    }
}
