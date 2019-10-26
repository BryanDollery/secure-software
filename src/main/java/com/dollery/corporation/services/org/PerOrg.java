package com.dollery.corporation.services.org;

import com.dollery.corporation.services.Output;
import com.dollery.corporation.services.agents.Agent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

public abstract class PerOrg<T extends PerOrg> {
    protected final String name;
    private final String id = UUID.randomUUID().toString();
    public Set<Agent> agents = new HashSet<>();
    public Products products;
    public Services services;
    private T parent;
    private Set<T> children = new HashSet<>();

    PerOrg(String name) {
        this.name = name;
    }

    public abstract T add(String name);

    public T getParent() {
        return parent;
    }

    void setParent(T parent) {
        this.parent = parent;
    }

    protected void add(T child) {
        child.setParent(this);
        children.add(child);
    }

    public T get(String name) {
        Optional<T> first = children.parallelStream().filter(p -> p.name.equals(name)).findFirst();
        if (!first.isPresent()) throw new RuntimeException("Could not find " + name);
        return first.get();
    }

    public Set<T> get() {
        return children;
    }

    @Override
    public String toString() {
        String q = Output.PLAIN_QUOTE;
        String c = ":";
        String kids = children.parallelStream().map(PerOrg::toString).collect(joining(", "));
        String s = "{" + q + name + q + ": [" + kids + "]}";
        return s;

//        String kids = children.parallelStream().map(PerOrg::toString).collect(joining(", "));
//        return "\"" + name + "\"" + (kids.length() == 0 ? "" : " : [" + kids + "] ");

    }
}
