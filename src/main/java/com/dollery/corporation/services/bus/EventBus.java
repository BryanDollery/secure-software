package com.dollery.corporation.services.bus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.dollery.corporation.services.Colors.CYAN_BRIGHT;
import static com.dollery.corporation.services.Colors.RESET;
import static com.dollery.corporation.services.bus.EventBus.ReplayMode.AllTags;
import static java.util.stream.Collectors.toList;

/**
 * A remarkably simple event bus
 */
public class EventBus {
    private static final Logger log = LoggerFactory.getLogger(EventBus.class);

    private List<Event> events = new ArrayList<>();
    private Map<String, Set<Subscriber>> subscribers = new HashMap<>();

    public EventBus publish(String body, String... tags) {
        Event event = new Event(body, tags);
        events.add(event);
        log.info("Event: {}", event);
        notifyAllTags(event);
        return this;
    }

    public EventBus subscribe(String tag, Subscriber subscriber) {
        if (!subscribers.containsKey(tag)) subscribers.put(tag, new HashSet<>());
        Set<Subscriber> subs = this.subscribers.get(tag);
        subs.add(subscriber);
        log.info("Subscribe: {}{}{}", CYAN_BRIGHT, tag, RESET);
        return this;
    }

    private Set<Subscriber> getSubscribers(String tag) {
        if (!subscribers.containsKey(tag)) subscribers.put(tag, new HashSet<>());

        return this.subscribers.get(tag);
    }

    public List<Event> getEvents(String tag) {
        return events.parallelStream().filter(e -> e.hasTag(tag)).collect(toList());
    }

    public List<Event> getPreviousEvents(String tag, int count) {
        return getEvents(tag).subList(events.size() - count, events.size());
    }

    public List<Event> getSince(String tag, Instant timestamp) {
        return events.parallelStream().filter(e -> e.getTimestamp().isAfter(timestamp) && e.hasTag(tag)).collect(toList());
    }

    public void replay(String tag, ReplayMode replayMode) {
        getEvents(tag).stream().forEach(replayMode == AllTags ? this::notifyAllTags : notifySubscribersOnlyToTheReplayTag(tag));
    }

    private Consumer<Event> notifySubscribersOnlyToTheReplayTag(String tag) {
        return e -> getSubscribers(tag).parallelStream().forEach(s -> s.handle(e));
    }

    private void notifyAllTags(Event event) {
        event.getTags().parallelStream().map(t -> getSubscribers(t)).flatMap(Collection::parallelStream).distinct().forEach(s -> s.handle(event));
    }

    enum ReplayMode {
        OnlyThisTag, AllTags
    }
}
