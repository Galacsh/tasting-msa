package com.galacsh;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface SessionToPassport {
    @GetExchange("/passports")
    Mono<Response<Passport>> exchange(@RequestParam("vvrite_id") String vvriteId);
}
