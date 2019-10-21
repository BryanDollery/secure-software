package com.dollery.services.vault.model;

import static java.lang.String.format;

public class Relationship {
    private Service server;
    private String role;

    Relationship(Service server) {
        this.server = server;
    }

    public String path() {
        return format("/%s/%s", server.getName(), role);
    }

    Service getServer() {
        return server;
    }

    String getRole() {
        return role;
    }

    public Relationship as(String role) {
        this.role = role;
        return this;
    }
}
