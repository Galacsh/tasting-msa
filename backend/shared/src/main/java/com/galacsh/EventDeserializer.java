package com.galacsh;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.io.IOException;

public class EventDeserializer extends JsonSerde<Event> {

    public EventDeserializer() {
        super(Event.class, customizedObjectMapper());
    }

    private static ObjectMapper customizedObjectMapper() {
        ObjectMapper mapper = JacksonUtils.enhancedObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Event.class, new JacksonEventDeserializer(Event.class));
        mapper.registerModule(module);
        return mapper;
    }

    private static class JacksonEventDeserializer extends StdDeserializer<Event> {

        protected JacksonEventDeserializer(Class<?> valueClass) {
            super(valueClass);
        }

        public Event deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);

            EventType eventType = EventType.valueOf(node.get("eventType").asText());
            long timestamp = node.get("timestamp").asLong();

            var payload = switch (eventType) {
                case USER_SIGN_UP, USER_DELETE:
                    yield new EventPayload.UsernameOnly(node.get("payload").get("username").asText());
            };

            return new Event(eventType, payload, timestamp);
        }

    }
}
