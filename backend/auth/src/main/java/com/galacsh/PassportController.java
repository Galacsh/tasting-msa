package com.galacsh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController {
    @GetMapping("/passports")
    public Response<Passport> exchangeToPassport(@RequestParam("vvrite_id") String sessionId) {
        // Simulate successful exchange
        Passport passport = new Passport(sessionId.toUpperCase());
        return Response.ok(passport);
    }
}
