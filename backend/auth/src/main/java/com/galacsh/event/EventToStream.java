package com.galacsh.event;

import com.galacsh.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventToStream {
    private static final Logger log = LoggerFactory.getLogger(EventToStream.class);

    private final StreamBridge streamBridge;

    public EventToStream(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Async
    @EventListener({ Event.class })
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignUpEvent(Event event) {
        log.debug("Received event after commit: {}", event);
        streamBridge.send("output", event);
    }
}
