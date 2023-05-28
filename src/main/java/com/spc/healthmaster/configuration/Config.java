package com.spc.healthmaster.configuration;

import com.google.common.collect.ImmutableMap;
import com.spc.healthmaster.command.CommandAction;
import com.spc.healthmaster.dtos.SshManagerDto;
import com.spc.healthmaster.enums.Action;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.spc.healthmaster.command.DownloadCommandAction.DOWNLOAD_COMMAND_ACTION;
import static com.spc.healthmaster.command.LogCommandAction.LOG_COMMAND_ACTION;
import static com.spc.healthmaster.command.StartCommandAction.START_COMMAND_ACTION;
import static com.spc.healthmaster.command.StatusCommandAction.STATUS_COMMAND_ACTION;
import static com.spc.healthmaster.command.StopCommandAction.STOP_COMMAND_ACTION;

@Configuration
@EnableAsync
public class Config {

    @Bean
    public Map<Long, SshManagerDto> sshManager() {
        return  new ConcurrentHashMap<>();
    }

    @Bean
    public  Map<Action, CommandAction> actionMap(
            @Qualifier(START_COMMAND_ACTION) CommandAction startAction,
            @Qualifier(STATUS_COMMAND_ACTION) CommandAction statusAction,
            @Qualifier(STOP_COMMAND_ACTION) CommandAction stopAction,
            @Qualifier(DOWNLOAD_COMMAND_ACTION) CommandAction downloadAction,
            @Qualifier(LOG_COMMAND_ACTION) CommandAction logAction
    ) {

        return ImmutableMap.of(
            Action.START, startAction,
            Action.STATUS, statusAction,
            Action.STOP, stopAction,
            Action.LOG, logAction,
            Action.DOWNLOAD, downloadAction
        );
    }
}
