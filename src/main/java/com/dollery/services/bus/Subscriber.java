package com.dollery.services.bus;

public interface Subscriber {
    void handle(Event event);
}
