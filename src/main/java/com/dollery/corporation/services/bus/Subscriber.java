package com.dollery.corporation.services.bus;

public interface Subscriber {
    void handle(Event event);
}
