package com.galacsh;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

@RestController
public class Controller {
    @GetMapping("/hello")
    public String hello(@RequestHeader(name = "X-Passport", required = false) String passport) {
        return "Hello, " + (passport == null ? "stranger" : passport);
    }

    @GetMapping("/4xx")
    public void four() {
        throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/5xx")
    public void five() {
        throw new ServerErrorException("Simulated server error", null);
    }
}
