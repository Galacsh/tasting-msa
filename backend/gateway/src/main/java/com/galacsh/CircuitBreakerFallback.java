package com.galacsh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;

@RestController
public class CircuitBreakerFallback {
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerFallback.class);

    @GetMapping("/circuit-breaker-fallback")
    public String fallback(ServerWebExchange exchange) {
        Throwable exception = exchange.getAttribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);

        if (exception == null) {
            return "TODO: Message that direct call to this endpoint is not allowed.";
        }

        log.error("Circuit breaker caught exception.", exception);
        return "TODO: Circuit breaker fallback message";
    }
}
