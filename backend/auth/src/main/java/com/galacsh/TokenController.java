package com.galacsh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @GetMapping("/tokens/validity")
    public boolean validateToken() {
        return Math.random() < 0.5;
    }
}
