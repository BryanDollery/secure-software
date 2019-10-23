package com.dollery.corporation.services.vault.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicesTest {
    @Test
    void singleService() {
        Service a = new Service("A");
        assertNotNull(a);

    }

    @Test
    void clientToAService() {
        Service orderReporter = new Service("orderReporter");
        Service orderDatabase = new Service("orderDB");
        orderReporter.uses(orderDatabase).as("reporter");
        Service customerDatabase = new Service("customerDatabase");
        orderReporter.uses(customerDatabase).as("customerOnboarder");
        String policies = orderReporter.getPolicies().toString();
        assertTrue(policies.contains("\"orderDB\" : \"rw\",  \"reporter\" : \"r\""));
        assertTrue(policies.contains("\"customerOnboarder\" : \"r\",  \"customerDatabase\" : \"rw\""));
    }

    @Test
    void generatePolicy() {
        Service a = new Service("a");
        Service b = new Service("b");
        a.uses(b).as("role");
        a.getPolicies();
    }
}
