package com.galacsh.controller;

import com.galacsh.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @PostMapping("/signup")
    public Response<?> signup() {
        throw new RuntimeException("Not implemented yet");
    }

    @PostMapping("/login")
    public Response<?> login() {
        throw new RuntimeException("Not implemented yet");
    }

    @GetMapping("/logout")
    public Response<?> logout() {
        throw new RuntimeException("Not implemented yet");
    }
}
