package com.galacsh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigClientTest implements ApplicationRunner {

    @Value("${message}")
    private String message;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Message: " + message);
    }
}
