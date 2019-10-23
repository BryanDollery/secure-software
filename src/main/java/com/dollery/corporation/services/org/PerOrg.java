package com.dollery.corporation.services.org;

import com.dollery.corporation.services.agents.Agent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

public class PerOrg {
    private final String id = UUID.randomUUID().toString();
    private final String name;
    public Set<Agent> agents = new HashSet<>();
    public Products products;
    public Services services;
    private PerOrg parent;
    private Set<PerOrg> children = new HashSet<>();

    public PerOrg(String name) {
        this.name = name;
    }

    private PerOrg(String name, PerOrg parent) {
        this(name);
        this.parent = parent;
    }

    public PerOrg add(String name) {
        children.add(new PerOrg(name, this));
        return this;
    }

    protected PerOrg add(PerOrg child) {
        child.parent = this;
        children.add(child);
        return this;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public PerOrg get(String name) {
        Optional<PerOrg> first = children.parallelStream().filter(p -> p.name.equals(name)).findFirst();
        if (!first.isPresent()) throw new RuntimeException("Could not find " + name);
        return first.get();
    }

    @Override
    public String toString() {
        String kids = children.parallelStream().map(PerOrg::toString).collect(joining(", "));
        return name + (kids.length() == 0 ? "" : " -> {" + kids + "} ");
    }
}
