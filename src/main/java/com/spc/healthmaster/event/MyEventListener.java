package com.spc.healthmaster.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MyEventListener {

    @EventListener
    public void handleCustomEvent(MyCustomEvent event) {
        String message = event.getMessage();
        try {

            System.out.println("mensaje 1");
            // Agrega un retraso de 1 segundo
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            // Manejo de excepciones si es necesario
        }
        System.out.println("mensaje 2");
        // Lógica para manejar el evento
    }
}

