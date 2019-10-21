package com.dollery.services.vault.model;

import static java.lang.String.format;

public class Relationship {
    private Service server;
    private String role;

    public Relationship(Service server) {
        this.server = server;
    }

    public String path() {
        return format("/%s/%s", server.getName(), role);
    }

    public Service getServer() {
        return server;
    }

    public String getRole() {
        return role;
    }

    public Relationship as(String role) {
        this.role = role;
        return this;
    }
}
