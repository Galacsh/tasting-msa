package com.galacsh;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
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
