package com.dollery.services.vault;

import com.dollery.services.bus.EventBus;
import com.dollery.services.catalog.Catalog;
import com.dollery.services.catalog.ControlledEnvironment;
import com.dollery.services.catalog.StandingCommittees;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VaultCatalogEventDrivenPolicySourceForControlledEnvironmentTest {
    private EventBus bus = new EventBus();
    private Catalog catalog = new Catalog(bus);
    private Vault vault = new Vault(bus);
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

        // then
        assertTrue(catalog.getServicesJson().contains("\"a\""), "Didn't find service A");
        assertTrue(catalog.getServicesJson().contains("\"b\""), "Didn't find service B");
        assertTrue(catalog.getServicesJson().contains("\"c\""), "Didn't find service C");

        assertEquals(1, vault.getPolicyCount(), "There should be a policy at this point");
        assertEquals(1, vault.getSecretCount(), "There should be a secret at this point");

        String secret = vault.fetch("a", "/dev/latest/secrets/b");
        assertEquals("{}", secret, "The secret should exist, but be an empty json object");
        vault.store("b", "/dev/latest/secrets/b", "{username:\"wibble\",password:\"wobble\"}");
        secret = vault.fetch("a", "/dev/latest/secrets/b");
        assertEquals("{username:\"wibble\",password:\"wobble\"}", secret, "We should have credentials here");
    }
}
