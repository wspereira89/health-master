package com.spc.healthmaster.event;

import org.springframework.context.ApplicationEvent;

public class MaintenanceEvent extends ApplicationEvent {

    private String message;

    public MaintenanceEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
