package com.dollery.corporation.services.catalog;

import com.dollery.corporation.services.bus.EventBus;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * There should be one of these for each class of deployment
 * dev, test, prod, etc.
 * <p>
 * The aim of the first stage of the software delivery process is to get into the catalog,
 * then get sufficient approvals within the catalog for 'production' use (which means to start returning the value
 * it was created to return).
 */
public class Catalog {
    public static final String CATALOG_CHANGED_EVENT_TAG = "catalog-changed";

    private Map<String, Service> services = new HashMap<>();
    private Set<Relationship> relationships = new HashSet<>();
    private EventBus bus;

    public Catalog(EventBus bus) {
        this.bus = bus;
    }

    public void addService(String serviceName) {
        services.put(serviceName, new Service(serviceName));
        bus.publish("+" + serviceName, CATALOG_CHANGED_EVENT_TAG);
    }

    public void addRelationship(String fromService, String toService) {
        relationships.add(new Relationship(services.get(fromService), services.get(toService)));
        bus.publish(">" + fromService + ":" + toService, CATALOG_CHANGED_EVENT_TAG);
    }

    /**
     * Name matches on
     *
     * @param providerName
     * @return
     */
    public Set<Relationship> relationshipsByProviderName(String providerName) {
        return relationships.parallelStream().filter(r -> r.getProviderName().equals(providerName)).collect(toSet());
    }

    public Set<Relationship> relationshipsByConsumerName(String consumerName) {
        return relationships.parallelStream().filter(r -> r.getConsumerName().equals(consumerName)).collect(toSet());
    }

    public Collection<Service> getServices() {
        return this.services.values();
    }

    public String getServicesJson() {
        String services = this.services.values().parallelStream().sorted().map(Service::toString).collect(joining(","));
        return format("{\"services\" : [%s]}", services);
    }

    public boolean hasRelationships() {
        return this.relationships.size() > 0;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public String getRelationshipsJson() {
        String relationships = this.relationships.parallelStream().sorted().map(Relationship::toString).collect(joining(","));
        return format("[%s]", relationships);
    }

    public String getChangeEventName() {
        return CATALOG_CHANGED_EVENT_TAG;
    }

    public Service getService(String name) {
        return services.get(name);
    }
}
