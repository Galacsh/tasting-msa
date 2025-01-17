package com.galacsh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class ValidateToken extends AbstractGatewayFilterFactory<ValidateToken.Config> {

    private final TokenValidator tokenValidator;
    private final ReactiveCircuitBreaker circuitBreaker;
    private static final Logger log = LoggerFactory.getLogger(ValidateToken.class);
    private static final byte[] UNAUTHORIZED_RESPONSE = "{\"message\":\"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);

    public ValidateToken(TokenValidator tokenValidator, ReactiveCircuitBreakerFactory<?, ?> cbFactory) {
        super(Config.class);
        this.tokenValidator = tokenValidator;
        this.circuitBreaker = cbFactory.create("validate-token");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Mono<Boolean> validation = circuitBreaker.run(
                    tokenValidator.validate(),
                    this::fallback
            );

            return validation.flatMap(isValid -> {
                if (isValid) {
                    return chain.filter(exchange);
                } else {
                    return rejectRequest(exchange);
                }
            });
        };
    }

    private Mono<Boolean> fallback(Throwable throwable) {
        log.debug("Encountered an error validating token. Fallback to false");
        log.error("Circuit breaker caught an exception: ", throwable);
        return Mono.just(false);
    }

    private Mono<Void> rejectRequest(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return response.writeWith(Mono.fromSupplier(() ->
                response.bufferFactory().wrap(UNAUTHORIZED_RESPONSE)
        ));
    }

    public static class Config {
        // No configuration properties required
    }
}
