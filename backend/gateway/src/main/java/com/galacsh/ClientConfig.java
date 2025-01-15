package com.galacsh;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    /**
     * WebClient built with this builder will be load balanced.
     * Load balanced web clients can find the service by name in the registry.
     * @return Load balanced WebClient builder
     */
    @Bean
    @LoadBalanced
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebClient.Builder loadBalancedBuilder() {
        return WebClient.builder();
    }

    /**
     * An
     * @return HTTP Interface Client made with load balanced WebClient.
     */
    @Bean
    public AuthService authService() {
        WebClient client = loadBalancedBuilder()
                .baseUrl("lb://auth")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client))
                .build();

        return factory.createClient(AuthService.class);
    }
}
