package com.dollery.corporation.services.vault.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.dollery.corporation.services.Colors.DATA;
import static com.dollery.corporation.services.Colors.SHADE;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Policy {
    private static final Logger log = LoggerFactory.getLogger(Policy.class);

    private static final String template = "  \"%s\" : \"%s\"";
    public final UUID UUID = java.util.UUID.randomUUID();

    private final String id = UUID.toString();

    private String path;
    private Map<String, AccessRule> rules = new HashMap<>();

    public Policy(String path) {
        this.path = path;
        log.info("Policy created for {} ({})", DATA.color(path), SHADE.color(id));

    }

    public void addConsumer(String role) {
        this.rules.put(role, new AccessRule(role, AccessMode.r));
        log.info("Consumer added to policy role {} ({})", DATA.color(role), SHADE.color(id));
    }

    public void addOwner(String role) {
        this.rules.put(role, new AccessRule(role, AccessMode.rw));
        log.info("Owner added to policy role {} ({})", DATA.color(role), SHADE.color(id));
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
        //        private Set<String> writeOnlyRoles = new HashSet<>();
        private Set<String> readWriteRoles = new HashSet<>();

        Factory(String service) {
            this.service = service;
        }

        public static Factory path(String role) {
            return new Factory(role);
        }

        static Factory path(Service service, String role) {
            return new Factory("/" + service.getName() + "/" + role);
        }

        public Factory consumer(String role) {
            readOnlyRoles.add(role);
            return this;
        }

        public Factory owner(String role) {
            readWriteRoles.add(role);
            return this;
        }

        public Policy make() {
            Policy policy = new Policy(service);

            readOnlyRoles.parallelStream().forEach(policy::addConsumer);
            readWriteRoles.parallelStream().forEach(policy::addOwner);

            return policy;
        }
    }
}
