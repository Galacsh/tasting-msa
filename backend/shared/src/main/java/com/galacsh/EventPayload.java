package com.galacsh;

public interface EventPayload {
    record UsernameOnly(String username) implements EventPayload {}
}
