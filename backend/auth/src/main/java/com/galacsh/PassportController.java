package com.galacsh;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class PassportController {
    @GetMapping("/passports")
    public Passport exchangeToPassport(@RequestParam("vvrite_id") String sessionId) {
        double random = Math.random();

        // Simulate unexpected server error
        if (random < 0.33) {
            throw new RuntimeException("Simulated server error");
        }
        // Simulate passport not found
        else if (random < 0.66) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        // Simulate successful exchange
        else {
            return new Passport(sessionId.toUpperCase());
        }
    }
}
