package com.spc.healthmaster.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MyEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public MyEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    public void publishEvent(String message) {
        MyCustomEvent event = new MyCustomEvent(this, message);
        eventPublisher.publishEvent(event);
    }
}
