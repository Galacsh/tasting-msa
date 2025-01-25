package com.galacsh.controller;

import com.galacsh.Passport;
import com.galacsh.Response;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassportController {
    @GetMapping("/passports")
    public Response<Passport> exchangeToPassport(Authentication authentication) {
        var passport = new Passport(authentication.getName());
        return Response.ok(passport);
    }
}
