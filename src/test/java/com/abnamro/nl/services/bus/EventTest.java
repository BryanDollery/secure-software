package com.abnamro.nl.services.bus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventTest {
    @Test
    public void lowercaseTags() {
        Event event = new Event("body", "TAG1", "TAG2");
        assertTrue(event.getTags().contains("tag1"));
        assertTrue(event.getTags().contains("tag2"));
    }

}
