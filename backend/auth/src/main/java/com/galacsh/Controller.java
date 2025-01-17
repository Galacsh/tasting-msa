package com.galacsh;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/hello")
    public String hello(@RequestHeader(name = "X-Passport", required = false) String passport) {
        return "Hello, " + (passport == null ? "stranger" : passport);
    }

    @GetMapping("/4xx")
    public ResponseEntity<?> four() {
        return ResponseEntity.badRequest().body("4xx error");
    }

    @GetMapping("/5xx")
    public ResponseEntity<?> five() {
        return ResponseEntity.internalServerError().body("5xx error");
    }
}
