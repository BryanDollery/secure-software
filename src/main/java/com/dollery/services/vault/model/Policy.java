package com.dollery.services.vault.model;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Policy {
    private static final String template = "  \"%s\" : \"%s\"";

    private String path;
    private Map<String, AccessRule> rules = new HashMap<>();

    public Policy(String path) {
        this.path = path;
    }

    public void addRule(String role, AccessMode access) {
        this.rules.put(role, new AccessRule(role, access));
    }

    public void addClient(String role) {
        this.rules.put(role, new AccessRule(role, AccessMode.r));
    }

    public void addOwner(String role) {
        this.rules.put(role, new AccessRule(role, AccessMode.rw));
    }

    public void addWriteOnlyRole(String role) {
        this.rules.put(role, new AccessRule(role, AccessMode.w));
    }

    public String getPath() {
        return path;
    }

    public String toString() {
        return format("\"%s\" {%s}", this.getPath(), rules
                .values()
                .parallelStream()
                .map(rule -> format(template, rule.getRole(), rule.getAccess()))
                .collect(joining(",")));
    }

    public AccessRule getRule(String role) {
        return rules.get(role);
    }

    public static class Factory {
        private String service;
        private Set<String> readOnlyRoles = new HashSet<>();
        private Set<String> writeOnlyRoles = new HashSet<>();
        private Set<String> readWriteRoles = new HashSet<>();

        public Factory(String service) {
            this.service = service;
        }

        public static Factory path(String role) {
            return new Factory(role);
        }

        public static Factory path(Service service, String role) {
            return new Factory("/" + service.getName() + "/" + role);
        }

        public Factory readRole(String role) {
            readOnlyRoles.add(role);
            return this;
        }

        public Factory writeRole(String role) {
            writeOnlyRoles.add(role);
            return this;
        }

        public Factory readWriteRole(String role) {
            readWriteRoles.add(role);
            return this;
        }

        public Policy make() {
            Policy policy = new Policy(service);

            readOnlyRoles.parallelStream().forEach(policy::addClient);
            writeOnlyRoles.parallelStream().forEach(policy::addWriteOnlyRole);
            readWriteRoles.parallelStream().forEach(policy::addOwner);

            return policy;
        }
    }
}
