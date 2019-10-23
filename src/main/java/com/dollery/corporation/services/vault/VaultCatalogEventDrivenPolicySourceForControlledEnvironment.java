package com.dollery.corporation.services.vault;

import com.dollery.corporation.services.bus.Event;
import com.dollery.corporation.services.bus.EventBus;
import com.dollery.corporation.services.bus.Subscriber;
import com.dollery.corporation.services.catalog.ControlledEnvironment;
import com.dollery.corporation.services.catalog.Relationship;
import com.dollery.corporation.services.vault.model.Policy;
import com.dollery.corporation.services.catalog.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.dollery.corporation.services.Colors.BLUE;

/**
 * A policy generator for Vault that responds to catalog events for a given environment
 * <p>
 * So, it's a source of vault policies that have their data sourced from the catalog, but within the domain
 * of a controlled environment
 */
public class VaultCatalogEventDrivenPolicySourceForControlledEnvironment implements Subscriber {
    private static final Logger log = LoggerFactory.getLogger(VaultCatalogEventDrivenPolicySourceForControlledEnvironment.class);
    private final Catalog catalog;
    private final Vault vault;
    private ControlledEnvironment env;

    /**
     * Constructor
     *
     * @param bus     I'm going to subscribe to a channel on this bus
     * @param vault   I'm going to change policies in this Vault
     * @param catalog I'm going approved byto listen to events from this source and use its data to build Vault policies
     * @param env     I'm going to respond to events on this environment's channel
     */
    VaultCatalogEventDrivenPolicySourceForControlledEnvironment(EventBus bus, Vault vault, Catalog catalog, ControlledEnvironment env) {
        this.catalog = catalog;
        this.vault = vault;
        this.env = env;
        bus.subscribe(catalog.getChangeEventName(), this);
    }

    @Override
    public void handle(Event event) {
        // The catalog has changed
        log.debug("The {} catalog has changed", BLUE.color(env.getName()));
        vault.clearPolicies();

        if (!catalog.hasRelationships()) return;

        Set<Relationship> rels = catalog.getRelationships();
        rels.parallelStream().forEach(r -> {
            String path = "/" + env.getName() + "/secrets/" + r.getProviderName();
            Policy policy = new Policy(path);
            policy.addConsumer(r.getConsumerName());
            policy.addOwner(r.getProviderName());
            vault.addPolicy(policy);
            vault.store(r.getProviderName(), path, "{}");
        });
    }
}
