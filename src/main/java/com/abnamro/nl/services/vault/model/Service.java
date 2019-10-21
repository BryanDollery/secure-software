package com.abnamro.nl.services.vault.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.abnamro.nl.services.vault.model.AccessMode.r;

public class Service {
    private String id;
    private String name;
    private String desc;

    private Set<Relationship> relatilonships = new HashSet<>();

    public Service(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Relationship uses(Service service) {
        Relationship relationship = new Relationship(service);
        relatilonships.add(relationship);
        return relationship;
    }

    public Set<Policy> getPolicies() {
        Set<Policy> policies = new HashSet<>();

        for (Relationship rel : relatilonships) {
            AccessRule ar = new AccessRule(rel.getRole(), r);
            Policy policy = Policy.Factory.path(rel.getServer(), rel.getRole()).readRole(rel.getRole()).readWriteRole(rel.getServer().getName()).make();
            policies.add(policy);
        }

        return policies;
    }
}
