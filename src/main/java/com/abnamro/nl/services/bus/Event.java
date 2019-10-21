package com.abnamro.nl.services.bus;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static com.abnamro.nl.services.Colors.BLACK_BRIGHT;
import static com.abnamro.nl.services.Colors.GREEN;
import static com.abnamro.nl.services.Colors.RED;
import static com.abnamro.nl.services.Colors.RESET;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Set.copyOf;
import static java.util.stream.Collectors.toSet;

public class Event implements Comparable<Event> {
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
        String symbol = BLACK_BRIGHT.toString();
        String label = BLACK_BRIGHT.toString();
        String bodyC = RED.toString();
        String tagC = GREEN.toString();
        String timestampC = BLACK_BRIGHT.toString();


        String formattedTime = FORMATTER.format(timestamp);

        return format(symbol + "{"
                + "\"" + label + "body" + symbol + "\":\"" + bodyC + "%s" + symbol + "\","
                + "\"" + label + "tags" + symbol + "\":\"" + tagC + "%s" + symbol + "\","
                + "\"" + label + "timestamp" + symbol + "\":\"" + timestampC + "%s" + symbol + "\""
                + "}" + RESET, body, tags, formattedTime);
    }

    public String toStringClear() {
        return format("{\"body\":\"%s\",\"timestamp\":\"%s\",\"tags\":\"%s\"}", body, timestamp, tags);
    }
}
