package com.dollery.corporation.services.bus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static com.dollery.corporation.services.Colors.GREEN;
import static com.dollery.corporation.services.Colors.RED;
import static com.dollery.corporation.services.Colors.SHADE;
import static com.dollery.corporation.services.Output.CLOSE_BRACE;
import static com.dollery.corporation.services.Output.COMMA;
import static com.dollery.corporation.services.Output.OPEN_BRACE;
import static com.dollery.corporation.services.Output.pair;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Set.copyOf;
import static java.util.stream.Collectors.toSet;

public class Event implements Comparable<Event> {
    @SuppressWarnings("WeakerAccess")
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").withZone(ZoneId.systemDefault());

    private String body;
    private Instant timestamp;
    private Set<String> tags;

    Event(String body, String... tag) {
        this.body = body;
        this.timestamp = Instant.now();
        this.tags = stream(tag).map(String::toLowerCase).collect(toSet());
        tags.add("all"); // special event tag
    }

    public String getBody() {
        return body;
    }

    Set<String> getTags() {
        return copyOf(tags);
    }

    Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Event event) {
        return timestamp.compareTo(event.timestamp);
    }

    boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    @Override
    public String toString() {
        return OPEN_BRACE
                + pair("body", body, RED) + COMMA
                + pair("tags", tags.toString(), GREEN) + COMMA
                + pair("timestamp", formats(timestamp), SHADE)
                + CLOSE_BRACE;
    }

    public String toStringClear() {
        return format("{\"body\":\"%s\",\"timestamp\":\"%s\",\"tags\":\"%s\"}", body, timestamp, tags);
    }

    private String formats(Instant timestamp) {
        return FORMATTER.format(timestamp);
    }
}
