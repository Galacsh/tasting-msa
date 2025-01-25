package com.galacsh;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface SessionToPassport {
    @GetExchange("/passports")
    Mono<Response<Passport>> exchange(@CookieValue("tmid") String sessionId);
}
