package com.dollery.corporation.services.org;

import com.dollery.corporation.services.agents.Agent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

public abstract class PerOrg<T extends PerOrg> {
    private final String id = UUID.randomUUID().toString();
    private final String name;
    public Set<Agent> agents = new HashSet<>();
    public Products products;
    public Services services;
    private PerOrg<T> parent;
    private Set<PerOrg<T>> children = new HashSet<>();

    PerOrg(String name) {
        this.name = name;
    }

    private PerOrg(String name, PerOrg<T> parent) {
        this(name);
        this.parent = parent;
    }

    public abstract T add(String name);

    PerOrg<T> setParent(PerOrg<T> parent) {
        this.parent = parent;
        return this;
    }

    protected PerOrg<T> add(PerOrg<T> child) {
        child.setParent(this);
        children.add(child);
        return this;
    }

    public PerOrg get(String name) {
        Optional<PerOrg<T>> first = children.parallelStream().filter(p -> p.name.equals(name)).findFirst();
        if (!first.isPresent()) throw new RuntimeException("Could not find " + name);
        return first.get();
    }

    @Override
    public String toString() {
        String kids = children.parallelStream().map(PerOrg::toString).collect(joining(", "));
        return name + (kids.length() == 0 ? "" : " -> {" + kids + "} ");
    }
}
