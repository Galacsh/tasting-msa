package com.galacsh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.FALSE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter(AuthService authService) {
        super(Config.class);
        this.authService = authService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            return authService.validateToken()
                    .flatMap(isValid -> {
                        if (FALSE.equals(isValid)) {
                            log.debug("Due to invalid token, request will be rejected. {}", request.getId());
                            return rejectRequest(response, "Invalid token");
                        }

                        log.trace("Token is valid. {}", request.getId());
                        return chain.filter(exchange);
                    })
                    .onErrorResume(e -> {
                        log.error("Request failed during checking token validity.", e);
                        log.debug("Due to request failure, request will be rejected. {}", request.getId());
                        return rejectRequest(response, "Failed to validate");
                    });
        };
    }

    private Mono<Void> rejectRequest(ServerHttpResponse response, String message) {
        response.setStatusCode(UNAUTHORIZED);
        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        DataBuffer body = response.bufferFactory()
                .wrap(String.format("{ \"message\": \"%s\"}", message).getBytes(UTF_8));

        return response.writeWith(Mono.just(body));
    }

    public static class Config {
        // No configuration properties required
    }
}
