package com.dollery.services.vault;

import com.dollery.services.bus.EventBus;
import com.dollery.services.vault.model.Policy;
import com.dollery.services.vault.model.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VaultTest {
    @Test
    public void basics() {
        EventBus bus = new EventBus();
        Vault vault = new Vault(bus);
        Policy policy = Policy.Factory.path("/orderDB/orderReporter").readRole("orderReporter").readWriteRole("owner").make();
        vault.addPolicy(policy);
        Service a = new Service("orderReporterService");
        Service b = new Service("orderDB");
        a.uses(b).as("orderReporter");

        vault.store("owner", "/orderDB/orderReporter", "{username:\"wibble\",password:\"wobble\"}");
        String creds = vault.fetch("orderReporter", "/orderDB/orderReporter");
        assertTrue(creds.contains("password"));
    }
}