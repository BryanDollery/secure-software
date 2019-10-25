package com.dollery.corporation.services.vault;

import com.dollery.corporation.services.bus.EventBus;
import com.dollery.corporation.services.catalog.Catalog;
import com.dollery.corporation.services.catalog.ControlledEnvironment;
import com.dollery.corporation.services.catalog.Service;
import com.dollery.corporation.services.org.SoftwareOrganisation;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VaultCatalogEventDrivenPolicySourceForControlledEnvironmentTest {
    private EventBus bus = new EventBus();
    private SoftwareOrganisation devO = new SoftwareOrganisation("dev", bus);
    private ControlledEnvironment dev = new ControlledEnvironment("dev", "latest", devO);
    private Catalog catalog = new Catalog(devO.getBus());
    private Vault vault = new Vault();

    @Test
    void policiesAreWrittenBasedOnCatalogEvents() {

        // A policy generator for Vault that responds to catalog events for a given environment
        new VaultCatalogEventDrivenPolicySourceForControlledEnvironment(devO.getBus(), vault, catalog, dev);

        //
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
