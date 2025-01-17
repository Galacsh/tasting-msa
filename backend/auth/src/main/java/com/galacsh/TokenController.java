package com.galacsh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @GetMapping("/tokens/validity")
    public boolean validateToken() {
        double random = Math.random();

        if (random < 0.33) {
            throw new RuntimeException("Random error");
        } else {
            return random < 0.66;
        }
    }
}
