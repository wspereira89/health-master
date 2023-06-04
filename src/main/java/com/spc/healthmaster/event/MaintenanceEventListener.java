package com.spc.healthmaster.event;

import com.spc.healthmaster.dtos.request.CommandRequestDto;
import com.spc.healthmaster.exception.ApiException;
import com.spc.healthmaster.services.commands.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MaintenanceEventListener {
    private final CommandService commandService;

    public MaintenanceEventListener(final CommandService commandService){
        this.commandService = commandService;
    }

    @EventListener
    public void handleCustomEvent(MaintenanceEvent event) {
        final List<CommandRequestDto> list = new ArrayList();
        try {

            log.error("saluods 1");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.error("saluods");
        list
           .parallelStream()
           .forEach(commandRequestDto -> {
                try {
                    commandService.executeCommand(commandRequestDto);
                } catch (final ApiException e) {
                    log.error("An unexpected error occurred",e);
                }
           });
    }
}

