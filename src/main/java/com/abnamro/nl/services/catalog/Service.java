package com.abnamro.nl.services.catalog;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

public class Service implements Comparable<Service> {
    private String id = randomUUID().toString();
    private SemVer version = new SemVer();
    private String name;

    Service(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public SemVer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return format("\"%s\"", this.name);
    }

    @Override
    public int compareTo(Service service) {
        return name.compareTo(service.name);
    }
}
