package com.abnamro.nl.services.vault;

import com.abnamro.nl.services.bus.Event;
import com.abnamro.nl.services.bus.EventBus;
import com.abnamro.nl.services.bus.Subscriber;
import com.abnamro.nl.services.catalog.Catalog;
import com.abnamro.nl.services.catalog.ControlledEnvironment;
import com.abnamro.nl.services.vault.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.abnamro.nl.services.Colors.BLUE;
import static com.abnamro.nl.services.Colors.RESET;

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
     * @param catalog I'm going to listen to events from this source and use its data to build Vault policies
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
        log.debug("The {}{}{} catalog has changed", BLUE, env.getName(), RESET);
        vault.clearPolicies();

        if (!catalog.hasRelationships()) return;

        String relationships = catalog.getRelationshipsJson();

        for (String rel : relationships.substring(12, relationships.length() - 2).trim().split(", ")) {
            String clean = rel.trim().replaceAll("\"", "");
            String s1 = clean.substring(0, clean.indexOf(','));
            String s2 = clean.substring(clean.indexOf(':') + 1);
            Policy policy = new Policy("/" + env.getName() + "/secrets/" + s2);
            policy.addClient(s1);
            policy.addOwner(s2);
            vault.addPolicy(policy);
            vault.store(s2, "/" + env.getName() + "/secrets/" + s2, "{}");
        }
    }
}
