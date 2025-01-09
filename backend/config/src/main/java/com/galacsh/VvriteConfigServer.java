package com.galacsh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class VvriteConfigServer {
    public static void main(String[] args) {
        SpringApplication.run(VvriteConfigServer.class, args);
    }
}