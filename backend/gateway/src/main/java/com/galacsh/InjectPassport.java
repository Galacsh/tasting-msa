package com.galacsh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class InjectPassport extends AbstractGatewayFilterFactory<InjectPassport.Config> {

    private final SessionToPassport sessionToPassport;
    private final ReactiveCircuitBreaker circuitBreaker;
    private static final Logger log = LoggerFactory.getLogger(InjectPassport.class);

    // TODO: Use shared class
    private static final byte[] UNAUTHORIZED_RESPONSE = "{\"message\":\"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);

    public InjectPassport(SessionToPassport sessionToPassport, ReactiveCircuitBreakerFactory<?, ?> cbFactory) {
        super(Config.class);
        this.sessionToPassport = sessionToPassport;
        this.circuitBreaker = cbFactory.create("passport");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var response = exchange.getResponse();

            var session = sessionCookieFrom(request);
            if (session.isEmpty()) return rejectRequest(response);

            Mono<Passport> exchangeToPassport = circuitBreaker.run(
                    sessionToPassport.exchange(session.get().getValue()),
                    throwable -> fallback(throwable, response)
            );

            return exchangeToPassport.flatMap(passport -> {
                if (passport == null) return rejectRequest(response);

                var passportInjected = exchange.getRequest().mutate()
                        .headers(httpHeaders -> httpHeaders.set("X-Passport", passport.id()))
                        .build();

                return chain.filter(exchange.mutate().request(passportInjected).build());
            });
        };
    }

    private Mono<Passport> fallback(Throwable throwable, ServerHttpResponse response) {
        log.error("Encountered an error while exchanging to passport. Request will be rejected.", throwable);

        return rejectRequest(response)
                .then(Mono.empty());
    }

    private Mono<Void> rejectRequest(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return response.writeWith(Mono.fromSupplier(() ->
                response.bufferFactory().wrap(UNAUTHORIZED_RESPONSE)
        ));
    }

    private Optional<HttpCookie> sessionCookieFrom(ServerHttpRequest request) {
        return Optional.ofNullable(request.getCookies().getFirst("vvrite_id"));
    }

    public static class Config {
        // No configuration properties required
    }
}
