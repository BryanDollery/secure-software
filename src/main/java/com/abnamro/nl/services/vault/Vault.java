package com.abnamro.nl.services.vault;

import com.abnamro.nl.services.bus.EventBus;
import com.abnamro.nl.services.vault.model.AccessRule;
import com.abnamro.nl.services.vault.model.Policy;

import java.util.HashMap;
import java.util.Map;

import static com.abnamro.nl.services.vault.model.AccessMode.*;

public class Vault {
    private Map<String, Policy> policies = new HashMap<>();
    private Map<String, String> secrets = new HashMap<>();
    private EventBus bus;

    public Vault(EventBus bus) {
        this.bus = bus;
    }

    public Vault store(String role, String path, String secret) {
        if (!canWrite(role, path)) throw new RuntimeException("Can't write");
        secrets.put(path, secret);
        return this;
    }

    public String fetch(String role, String path) {
        if (!canRead(role, path)) throw new RuntimeException("Can't read");

        return secrets.get(path);
    }

    private boolean canRead(String role, String path) {
        AccessRule accessRule = getAccessRule(role, path);

        return accessRule != null && (accessRule.isMode(r) || accessRule.isMode(rw));
    }

    private boolean canWrite(String role, String path) {
        AccessRule accessRule = getAccessRule(role, path);
        return accessRule != null && (accessRule.isMode(w) || accessRule.isMode(rw));
    }

    private AccessRule getAccessRule(String role, String path) {
        Policy policy = policies.get(path);
        if (policy == null) throw new RuntimeException("404 - Path: " + path + " not found");
        return policy.getRule(role);
    }

    public Vault addPolicy(Policy policy) {
        this.policies.put(policy.getPath(), policy);
        return this;
    }

    public Vault clearPolicies() {
        policies.clear();
        return this;
    }

    public int getPolicyCount() {
        return policies.size();
    }

    public int getSecretCount() {
        return secrets.size();
    }
}
