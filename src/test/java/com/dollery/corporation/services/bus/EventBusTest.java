package com.dollery.corporation.services.bus;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {
    @Test
    public void notifyByTag() {
        // given
        EventBus bus = new EventBus();

        AtomicBoolean triggered = new AtomicBoolean(false);
        AtomicBoolean error = new AtomicBoolean(false);

        bus.subscribe("t1", event -> triggered.set(true));
        bus.subscribe("t2", event -> error.set(true));

        // when
        bus.publish("E1", "t1");

        // then
        assertTrue(triggered.get());
        assertFalse(error.get());
    }

    @Test
    public void multipleSubscribersToASingleTagWithReplay() {
        // given
        EventBus bus = new EventBus();

        AtomicBoolean error = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);

        bus.subscribe("t1", event -> counter.incrementAndGet());

        // when
        bus.publish("E1", "t1");

        // then
        assertEquals(1, counter.get());
        bus.subscribe("t1", event -> counter.incrementAndGet());
        bus.subscribe("t2", event -> error.set(true));

        // and when
        bus.publish("E1", "t1");

        // then
        assertFalse(error.get());
        assertEquals(3, counter.get());

        // and then we replay
        bus.replay("t1", EventBus.ReplayMode.OnlyThisTag);
        assertEquals(7, counter.get());
    }
}
