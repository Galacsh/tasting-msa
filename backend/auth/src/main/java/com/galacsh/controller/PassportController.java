package com.galacsh.controller;

import com.galacsh.Passport;
import com.galacsh.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController {
    @GetMapping("/passports")
    public Response<Passport> exchangeToPassport(@RequestParam("vvrite_id") String sessionId) {
        throw new RuntimeException("Not implemented yet");
    }
}
