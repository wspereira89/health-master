package com.spc.healthmaster.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public MaintenanceEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    public void publishEvent(String message) {
        MaintenanceEvent event = new MaintenanceEvent(this, message);
        eventPublisher.publishEvent(event);
    }
}
