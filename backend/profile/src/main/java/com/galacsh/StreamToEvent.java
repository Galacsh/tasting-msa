package com.galacsh;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class StreamToEvent {

    private final ApplicationEventPublisher eventPublisher;

    public StreamToEvent(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Bean
    public EventDeserializer eventDeserializer() {
        return new EventDeserializer();
    }

    @Bean
    public Consumer<KStream<Void, Event>> auth() {
        return input -> input.foreach((unused, value) -> eventPublisher.publishEvent(value));
    }
}
