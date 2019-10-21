package com.abnamro.nl.services.bus;

public interface Subscriber {
    void handle(Event event);
}
