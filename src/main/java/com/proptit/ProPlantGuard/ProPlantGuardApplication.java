package com.proptit.ProPlantGuard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ProPlantGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProPlantGuardApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleAppStart(){
        MainApplication.launchApp();
    }
}