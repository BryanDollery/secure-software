package com.dollery.corporation.services.vault;

import com.dollery.corporation.services.bus.EventBus;
import com.dollery.corporation.services.catalog.ControlledEnvironment;
import com.dollery.corporation.services.catalog.Service;
import com.dollery.corporation.services.catalog.StandingCommittees;
import com.dollery.corporation.services.catalog.Catalog;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VaultCatalogEventDrivenPolicySourceForControlledEnvironmentTest {
    private EventBus bus = new EventBus();
    private Catalog catalog = new Catalog(bus);
    private Vault vault = new Vault();
    private StandingCommittees standingCommittees = new StandingCommittees(bus);
    private ControlledEnvironment dev = new ControlledEnvironment(bus, "dev", "latest", standingCommittees);

    @Test
    void policiesAreWrittenBasedOnCatalogEvents() {

        // A policy generator for Vault that responds to catalog events for a given environment
        new VaultCatalogEventDrivenPolicySourceForControlledEnvironment(bus, vault, catalog, dev);

        // given
        assertEquals(0, vault.getPolicyCount(), "There should be no policies at this point");
        assertEquals(0, vault.getSecretCount(), "There should be no secrets at this point");

        catalog.addService("a");
        catalog.addService("b");
        catalog.addService("c");

        // when
        catalog.addRelationship("a", "b");

        assertTrue(has(catalog.getServices(), "a"), "Didn't find service A");
        assertTrue(has(catalog.getServices(), "b"), "Didn't find service B");
        assertTrue(has(catalog.getServices(), "c"), "Didn't find service C");

        assertEquals(1, vault.getPolicyCount(), "There should be a policy at this point");
        assertEquals(1, vault.getSecretCount(), "There should be a secret at this point");

        String secret = vault.fetch("a", "/dev/latest/secrets/b");
        assertEquals("{}", secret, "The secret should exist, but be an empty json object");
        vault.store("b", "/dev/latest/secrets/b", "{username:\"wibble\",password:\"wobble\"}");
        secret = vault.fetch("a", "/dev/latest/secrets/b");
        assertEquals("{username:\"wibble\",password:\"wobble\"}", secret, "We should have credentials here");
    }

    private boolean has(Collection<Service> services, String name) {
        return services.parallelStream().anyMatch(s -> s.getName().equals(name));
    }
}