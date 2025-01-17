package com.galacsh;

import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface TokenValidator {
    @GetExchange("/tokens/validity")
    Mono<Boolean> validate();
}
