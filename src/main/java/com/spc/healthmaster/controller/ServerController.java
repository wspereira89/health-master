package com.spc.healthmaster.controller;


import com.spc.healthmaster.event.MyEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/server")
public class ServerController {
    private final MyEventPublisher eventPublisher;


    public ServerController(MyEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/event")
    public DeferredResult<Void> generateEvent() {
        DeferredResult<Void> deferredResult = new DeferredResult<>();

        String message = "Hello, event!";
        eventPublisher.publishEvent(message);

        deferredResult.setResult(null);
        return deferredResult;
    }

}
