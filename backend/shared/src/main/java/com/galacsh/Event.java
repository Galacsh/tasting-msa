package com.galacsh;

import java.time.Instant;

public class Event {
    private final EventType eventType;
    private final EventPayload payload;
    private final long timestamp;

    public Event(EventType eventType, EventPayload payload) {
        this(eventType, payload, Instant.now().toEpochMilli());
    }

    public Event(EventType eventType, EventPayload payload, long timestamp) {
        this.eventType = eventType;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventPayload getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Event { type: " + eventType.name() + ", timestamp: " + getTimestamp() + " }";
    }
}
