package com.dollery.services.catalog;

import com.dollery.services.bus.EventBus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.dollery.services.catalog.ControlledEnvironmentBuilder.AddingDependencies.complete;
import static com.dollery.services.catalog.ControlledEnvironmentBuilder.AddingDependencies.incomplete;
import static java.util.stream.Collectors.toSet;

public class ControlledEnvironmentBuilder {

    private EventBus bus;
    private String name;
    private String tag;
    private StandingCommittees standingCommittees;
    private Catalog catalog;
    private Set<Service> provides = new HashSet<>();
    private Set<Service> consumes = new HashSet<>();

    public ControlledEnvironment build() {
        ControlledEnvironment env = new ControlledEnvironment(bus, name, tag, standingCommittees);

        addDependencyTree();

        provides.parallelStream().forEach(s -> env.addService(s.getName()));
        consumes.parallelStream().forEach(s -> env.addService(s.getName()));

        return env;
    }

    /**
     *
     * For an environment to be complete, it must include every downstream service that is needed to provide it's own
     * service. These downstream services may not be deployed within a physical environment, they may instead be services
     * run by third-parties, but our model is interested in secure communication, not physical deployment models. Our
     * 'test' (controlled) environment is a map of service connections needed to fulfil this service in that context.
     * Though, it is possible to go much further with this model in a 100% k8s environment, where you can actually
     * deploy an entire controlled-environment in a single physical (ish*) environment. *namespace maybe.
     *
     * We are interested in downstream dependencies, so we just keep iterating down the tree until we have them all.
     * Circular dependencies are ok because we store services in Sets so any duplicate will be discarded.
     *
     * This is a really inefficient algorithm that in a production system would be outrageous
     * but in this simulation it's fine. This algorithm has a collection of services,
     * and it follows the relationships to downstream services and adds them to the environment.
     * At this point, it gets inefficient. Efficient algorithms would follow the trail to the next
     * level of dependencies. This one just starts again and repeats the whole thing. This means a
     * lot of work is redone examining services and relationships that have already been dealt with.
     * This algorithm assumes that there won't be so many services and relationships that java would
     * not be able to do the comparisons really fast, so it just brute forces it. If it had a thousand
     * services, the cost of dealing with a new dependency would require thousands of comparisons instead
     * of one. But, this is also fine, because this is a simulation and the aim here is to look at the
     * processes and flows, not the elegance and efficiency of the java code.
     *
     */
    private void addDependencyTree() {
        AddingDependencies ad = incomplete;

        do {
            ad = addDependencies();
        } while (ad == incomplete);
    }

    private AddingDependencies addDependencies() {
        int before = consumes.size();
        Set<Relationship> rels = provides.parallelStream()
                .map(s -> catalog.relationshipsByConsumerName(s.getName()))
                .flatMap(Collection::parallelStream)
                .collect(toSet());

        rels.parallelStream().forEach(r -> consumes.add(r.getProvider()));
        return before == consumes.size() ? complete : incomplete;
    }

    public ControlledEnvironmentBuilder provides(String service) {
        this.provides.add(catalog.getService(service));
        return this;
    }

    public ControlledEnvironmentBuilder from(Catalog catalog) {
        this.catalog = catalog;
        return this;
    }

    public ControlledEnvironmentBuilder on(EventBus bus) {
        this.bus = bus;
        return this;
    }

    public ControlledEnvironmentBuilder called(String name) {
        this.name = name;
        return this;
    }

    public ControlledEnvironmentBuilder tagWith(String tags) {
        this.name = tags;
        return this;
    }

    public ControlledEnvironmentBuilder governedBy(StandingCommittee standingCommittee) {
        this.standingCommittees.add(standingCommittee);
        return this;
    }

    enum AddingDependencies {complete, incomplete}

}
