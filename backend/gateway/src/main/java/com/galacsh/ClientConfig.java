package com.galacsh;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    /**
     * WebClient built with this builder will be load balanced.
     * Load balanced web clients can find the service by name in the registry.
     *
     * @return Load balanced WebClient builder
     */
    @Bean
    @LoadBalanced
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebClient.Builder loadBalancedBuilder() {
        return WebClient.builder();
    }

    /**
     * SessionToPassport client made with load balanced WebClient.
     * @return HTTP Interface Client
     */
    @Bean
    public SessionToPassport sessionToPassport() {
        WebClient client = loadBalancedBuilder()
                .baseUrl("lb://auth")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client))
                .build();

        return factory.createClient(SessionToPassport.class);
    }

    /**
     * Converts 5xx response to HttpServerErrorException
     * so that the circuit breaker can notice the error.
     *
     * @return HttpClientCustomizer that converts 5xx response to HttpServerErrorException
     */
    @Bean
    public HttpClientCustomizer gatewayHttpClientCustomizer() {
        return httpClient -> httpClient.doOnResponse(
                (res, conn) -> {
                    HttpStatus status = HttpStatus.valueOf(res.status().code());
                    if (status.is5xxServerError()) {
                        throw new HttpServerErrorException(status, status.getReasonPhrase());
                    }
                }
        );
    }
}
